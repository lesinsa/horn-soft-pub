package ru.prbb.common.biz;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Расширенные свойства. Повились в описании к задаче "СДК" - "Страница Дениса Кравченко".
 * Заполняются в {@link P2PService}, если на входе этот класс
 *
 * @author lesinsa on 08.07.2015
 */
public class P2PParamsExt extends P2PParams implements Serializable {
    @XmlElement(name = "COUNTRY", required = false, nillable = true)
    private String country;
    @XmlElement(name = "MERCH_GMT", required = false, nillable = true)
    private String merchantTimeZone;
    @XmlElement(name = "BRANDS", required = false, nillable = true)
    private String brands;

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMerchantTimeZone() {
        return merchantTimeZone;
    }

    public void setMerchantTimeZone(String merchantTimeZone) {
        this.merchantTimeZone = merchantTimeZone;
    }
}
