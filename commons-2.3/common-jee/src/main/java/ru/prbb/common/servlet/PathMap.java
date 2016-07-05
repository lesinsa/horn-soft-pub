//
//  ========================================================================
//  Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package ru.prbb.common.servlet;

import org.eclipse.jetty.util.LazyList;
import org.eclipse.jetty.util.StringMap;
import org.eclipse.jetty.util.URIUtil;

import java.util.*;

@SuppressWarnings("all")
public class PathMap extends HashMap {
    private static String pathSpecSeparators = ":,";

    private final transient StringMap prefixMap = new StringMap();
    private final transient StringMap suffixMap = new StringMap();
    private final transient StringMap exactMap = new StringMap();
    private transient List defaultSingletonList = null;
    private transient Entry prefixDefault = null;
    private transient Entry defaultEntry = null;
    private transient boolean nodeFault = false;

    public PathMap() {
        super(11);
    }

    public PathMap(boolean nodeFault) {
        super(11);
        this.nodeFault = nodeFault;
    }

    public PathMap(int capacity) {
        super(capacity);
    }

    public PathMap(Map m) {
        putAll(m);
    }

    public static void setPathSpecSeparators(String s) {
        pathSpecSeparators = s;
    }

    public static boolean match(String pathSpec, String path)
            throws IllegalArgumentException {
        return match(pathSpec, path, false);
    }

    public static boolean match(String pathSpec, String path, boolean noDefault)
            throws IllegalArgumentException {
        if (pathSpec.length() == 0)
            return "/".equals(path);

        char c = pathSpec.charAt(0);
        if (c == '/') {
            if (!noDefault && pathSpec.length() == 1 || pathSpec.equals(path))
                return true;

            if (isPathWildcardMatch(pathSpec, path))
                return true;
        } else if (c == '*')
            return path.regionMatches(path.length() - pathSpec.length() + 1,
                    pathSpec, 1, pathSpec.length() - 1);
        return false;
    }

    private static boolean isPathWildcardMatch(String pathSpec, String path) {
        // For a spec of "/foo/*" match "/foo" , "/foo/..." but not "/foobar"
        int cpl = pathSpec.length() - 2;
        if (pathSpec.endsWith("/*") && path.regionMatches(0, pathSpec, 0, cpl)) {
            if (path.length() == cpl || '/' == path.charAt(cpl))
                return true;
        }
        return false;
    }

    public static String pathMatch(String pathSpec, String path) {
        char c = pathSpec.charAt(0);

        if (c == '/') {
            if (pathSpec.length() == 1)
                return path;

            if (pathSpec.equals(path))
                return path;

            if (isPathWildcardMatch(pathSpec, path))
                return path.substring(0, pathSpec.length() - 2);
        } else if (c == '*') {
            if (path.regionMatches(path.length() - (pathSpec.length() - 1),
                    pathSpec, 1, pathSpec.length() - 1))
                return path;
        }
        return null;
    }

    public static String pathInfo(String pathSpec, String path) {
        if ("".equals(pathSpec))
            return path; //servlet 3 spec sec 12.2 will be '/'

        char c = pathSpec.charAt(0);

        if (c == '/') {
            if (pathSpec.length() == 1)
                return null;

            boolean wildcard = isPathWildcardMatch(pathSpec, path);

            // handle the case where pathSpec uses a wildcard and path info is "/*"
            if (pathSpec.equals(path) && !wildcard)
                return null;

            if (wildcard) {
                if (path.length() == pathSpec.length() - 2)
                    return null;
                return path.substring(pathSpec.length() - 2);
            }
        }
        return null;
    }

    public static String relativePath(String base,
                                      String pathSpec,
                                      String path) {
        String info = pathInfo(pathSpec, path);
        if (info == null)
            info = path;

        if (info.startsWith("./"))
            info = info.substring(2);
        if (base.endsWith(URIUtil.SLASH))
            if (info.startsWith(URIUtil.SLASH))
                path = base + info.substring(1);
            else
                path = base + info;
        else if (info.startsWith(URIUtil.SLASH))
            path = base + info;
        else
            path = base + URIUtil.SLASH + info;
        return path;
    }

    public void writeExternal(java.io.ObjectOutput out)
            throws java.io.IOException {
        HashMap map = new HashMap(this);
        out.writeObject(map);
    }

    public void readExternal(java.io.ObjectInput in)
            throws java.io.IOException, ClassNotFoundException {
        HashMap map = (HashMap) in.readObject();
        this.putAll(map);
    }

    @Override
    public Object put(Object pathSpec, Object object) {
        String str = pathSpec.toString();
        if ("".equals(str.trim())) {
            Entry entry = new Entry("", object);
            entry.setMapped("");
            exactMap.put("", entry);
            return super.put("", object);
        }

        StringTokenizer tok = new StringTokenizer(str, pathSpecSeparators);
        Object old = null;

        while (tok.hasMoreTokens()) {
            String spec = tok.nextToken();

            if (!spec.startsWith("/") && !spec.startsWith("*."))
                throw new IllegalArgumentException("PathSpec " + spec + ". must start with '/' or '*.'");

            old = super.put(spec, object);

            // Make entry that was just created.
            Entry entry = new Entry(spec, object);

            if (entry.getKey().equals(spec)) {
                if (spec.equals("/*"))
                    prefixDefault = entry;
                else if (spec.endsWith("/*")) {
                    String mapped = spec.substring(0, spec.length() - 2);
                    entry.setMapped(mapped);
                    prefixMap.put(mapped, entry);
                    exactMap.put(mapped, entry);
                    exactMap.put(spec.substring(0, spec.length() - 1), entry);
                } else if (spec.startsWith("*."))
                    suffixMap.put(spec.substring(2), entry);
                else if (spec.equals(URIUtil.SLASH)) {
                    if (nodeFault)
                        exactMap.put(spec, entry);
                    else {
                        defaultEntry = entry;
                        defaultSingletonList =
                                Collections.singletonList(defaultEntry);
                    }
                } else {
                    entry.setMapped(spec);
                    exactMap.put(spec, entry);
                }
            }
        }

        return old;
    }

    public Object match(String path) {
        Map.Entry entry = getMatch(path);
        if (entry != null)
            return entry.getValue();
        return null;
    }

    public Entry getMatch(String path) {
        Map.Entry entry = null;

        if (path == null)
            return null;

        int l = path.length();

        //special case
        if (l == 1 && path.charAt(0) == '/') {
            entry = (Map.Entry) exactMap.get("");
            if (entry != null)
                return (Entry) entry;
        }

        // try exact match
        entry = exactMap.getEntry(path, 0, l);
        if (entry != null)
            return (Entry) entry.getValue();

        // prefix search
        int i = l;
        while ((i = path.lastIndexOf('/', i - 1)) >= 0) {
            entry = prefixMap.getEntry(path, 0, i);
            if (entry != null)
                return (Entry) entry.getValue();
        }

        // Prefix Default
        if (prefixDefault != null)
            return prefixDefault;

        // Extension search
        i = 0;
        while ((i = path.indexOf('.', i + 1)) > 0) {
            entry = suffixMap.getEntry(path, i + 1, l - i - 1);
            if (entry != null)
                return (Entry) entry.getValue();
        }

        // Default
        return defaultEntry;
    }

    public Object getLazyMatches(String path) {
        Map.Entry entry;
        Object entries = null;

        if (path == null)
            return LazyList.getList(entries);

        int l = path.length();

        // try exact match
        entry = exactMap.getEntry(path, 0, l);
        if (entry != null)
            entries = LazyList.add(entries, entry.getValue());

        // prefix search
        int i = l - 1;
        while ((i = path.lastIndexOf('/', i - 1)) >= 0) {
            entry = prefixMap.getEntry(path, 0, i);
            if (entry != null)
                entries = LazyList.add(entries, entry.getValue());
        }

        // Prefix Default
        if (prefixDefault != null)
            entries = LazyList.add(entries, prefixDefault);

        // Extension search
        i = 0;
        while ((i = path.indexOf('.', i + 1)) > 0) {
            entry = suffixMap.getEntry(path, i + 1, l - i - 1);
            if (entry != null)
                entries = LazyList.add(entries, entry.getValue());
        }

        // Default
        if (defaultEntry != null) {
            // Optimization for just the default
            if (entries == null)
                return defaultSingletonList;

            entries = LazyList.add(entries, defaultEntry);
        }

        return entries;
    }

    public List getMatches(String path) {
        return LazyList.getList(getLazyMatches(path));
    }


    public boolean containsMatch(String path) {
        Entry match = getMatch(path);
        return match != null && !match.equals(defaultEntry);
    }

    @Override
    public Object remove(Object pathSpec) {
        if (pathSpec != null) {
            String spec = (String) pathSpec;
            if (spec.equals("/*"))
                prefixDefault = null;
            else if (spec.endsWith("/*")) {
                prefixMap.remove(spec.substring(0, spec.length() - 2));
                exactMap.remove(spec.substring(0, spec.length() - 1));
                exactMap.remove(spec.substring(0, spec.length() - 2));
            } else if (spec.startsWith("*."))
                suffixMap.remove(spec.substring(2));
            else if (spec.equals(URIUtil.SLASH)) {
                defaultEntry = null;
                defaultSingletonList = null;
            } else
                exactMap.remove(spec);
        }
        return super.remove(pathSpec);
    }

    @Override
    public void clear() {
        exactMap.clear();
        prefixMap.clear();
        suffixMap.clear();
        defaultEntry = null;
        defaultSingletonList = null;
        super.clear();
    }

    public static class Entry implements Map.Entry {
        private final Object key;
        private final Object value;
        private String mapped;
        private transient String string;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            if (string == null)
                string = key + "=" + value;
            return string;
        }

        public String getMapped() {
            return mapped;
        }

        void setMapped(String mapped) {
            this.mapped = mapped;
        }
    }
}