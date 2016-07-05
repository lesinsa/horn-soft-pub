package it.mock;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author LesinSA
 */
@XmlRootElement
public class CustomData {
    private Integer frontId;
    private String fio;

    public CustomData() {
    }

    public CustomData(Integer frontId, String fio) {
        this();
        this.frontId = frontId;
        this.fio = fio;
    }

    public Integer getFrontId() {
        return frontId;
    }

    public void setFrontId(Integer frontId) {
        this.frontId = frontId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomData that = (CustomData) o;

        if (fio != null ? !fio.equals(that.fio) : that.fio != null) return false;
        if (frontId != null ? !frontId.equals(that.frontId) : that.frontId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = frontId != null ? frontId.hashCode() : 0;
        result = 31 * result + (fio != null ? fio.hashCode() : 0);
        return result;
    }
}
