package ru.prbb.common.biz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.lang.String;

/**
 * @author lesinsa on 08.07.2015
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class P2PParams implements Serializable {
    @XmlElement(name = "AMOUNT", required = true, nillable = false)
    private String amount;
    @XmlElement(name = "BACKREF", required = true, nillable = false)
    private String backRef;
    @XmlElement(name = "CARD", nillable = false)
    private String card;
    @XmlElement(name = "CURRENCY", required = true, nillable = false)
    private String currency;
    @XmlElement(name = "CVC2", required = true, nillable = false)
    private String cvc2;
    @XmlElement(name = "CVC2_RC", required = true, nillable = false)
    private String cvc2rc;
    @XmlElement(name = "DESC", required = true, nillable = false)
    private String desc;
    @XmlElement(name = "EMAIL", required = true, nillable = false)
    private String email;
    @XmlElement(name = "EXP", required = true, nillable = false)
    private String expireMonth;
    @XmlElement(name = "EXP_YEAR", required = true, nillable = false)
    private String expireYear;
    @XmlElement(name = "MERCHANT", required = true, nillable = false)
    private String merchant;
    @XmlElement(name = "MERCHANT_NAME", required = true, nillable = false)
    private String merchantName;
    @XmlElement(name = "MERCH_URL", required = true, nillable = false)
    private String merchantUrl;
    @XmlElement(name = "NONCE", required = true, nillable = false)
    private String nonce;
    @XmlElement(name = "ORDER")
    private String order;
    @XmlElement(name = "PASSWORD", required = true, nillable = false)
    private String password;
    @XmlElement(name = "PAYMENT_TO", required = false, nillable = true)
    private String paymentTo;
    @XmlElement(name = "P_SIGN", required = true, nillable = false)
    private String sign;
    @XmlElement(name = "TERMINAL", required = true, nillable = false)
    private String terminal;
    @XmlElement(name = "TIMESTAMP", required = true, nillable = false)
    private String timestamp;
    @XmlElement(name = "TRTYPE", required = true, nillable = false)
    private String trType;
    @XmlElement(name = "PAN")
    private String pan;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBackRef() {
        return backRef;
    }

    public void setBackRef(String backRef) {
        this.backRef = backRef;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCvc2() {
        return cvc2;
    }

    public void setCvc2(String cvc2) {
        this.cvc2 = cvc2;
    }

    public String getCvc2rc() {
        return cvc2rc;
    }

    public void setCvc2rc(String cvc2rc) {
        this.cvc2rc = cvc2rc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentTo() {
        return paymentTo;
    }

    public void setPaymentTo(String paymentTo) {
        this.paymentTo = paymentTo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTrType() {
        return trType;
    }

    public void setTrType(String trType) {
        this.trType = trType;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
