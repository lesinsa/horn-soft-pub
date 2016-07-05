package ru.prbb.common.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.DatatypeConverter;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.prbb.common.NullUtils.coalesce;
import static ru.prbb.common.StringUtils.lpad;

/**
 * @author lesinsa on 08.07.2015
 */
@ApplicationScoped
public class P2PService {
    public static final String VISA_CODE = "VISA";
    public static final String MASTERCARD_CODE = "ECMC";
    static final Logger LOG = LoggerFactory.getLogger(P2PService.class);
    private static final String MAC_ALG = "HMACSHA1";

    private DateTimeFormatter formatter;

    @Inject
    private NonceGenerator nonceGenerator;
    @Inject
    private P2PConfig config;

    @PostConstruct
    public void init() {
        formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    }

    public P2PParams ecommPrepare(@NotNull @Valid P2PParams input, @NotNull String encodedKey, TrType trType) {
        // заполняем параметры
        putParamsOptionally(input, trType);
        return innerPrepare(input, encodedKey, trType);
    }

    private P2PParams innerPrepare(@NotNull @Valid P2PParams input, @NotNull String encodedKey, TrType trType) {
        input.setTrType(trType.getCode());
        // заполняем генерируемые свойства всегда, перезаписывая полученные значения
        if (input.getNonce() == null) {
            input.setNonce(nonceGenerator.next());
        }
        if (input.getTimestamp() == null) {
            input.setTimestamp(takeTimestamp());
        }
        if (input instanceof P2PParamsExt && ((P2PParamsExt) input).getBrands() != null) {
            ((P2PParamsExt) input).setBrands(takeBrand(takeBrand(input.getCard())));
        }

        // правим ORDER, если длина меньше 6 символов
        if (input.getOrder() == null) {
            throw new IllegalArgumentException("ORDER cannot be null");
        }
        input.setOrder(lpad(input.getOrder(), '0', 6));

        // формируем список ппараметров, входящих в подпись
        List<String> params = trType.getParamList(input);
        // рассчитываем MAC-подпись
        byte[] sign = calcHmac(encodedKey, params);
        // проставляем подпись в HEX-кодировке
        String signEncoded = DatatypeConverter.printHexBinary(sign).toLowerCase();
        LOG.debug("MAC={}", signEncoded);
        input.setSign(signEncoded);
        return input;
    }

    private void putParamsOptionally(@NotNull @Valid P2PParams input, TrType trType) {
        // заполняем опционально, если не прислали
        if (input.getPassword() == null) {
            input.setPassword(config.getPassword());
        }
        if (trType.isCvc2rc() && input.getCvc2rc() == null) {
            input.setCvc2rc(config.getCvc2rc());
        }
        if (input.getCurrency() == null) {
            input.setCurrency(config.getCurrency());
        }

        // расширенные свойства
        if (input instanceof P2PParamsExt) {
            P2PParamsExt ext = (P2PParamsExt) input;
            if (ext.getCountry() == null) {
                ext.setCountry(config.getCountry());
            }
            if (ext.getMerchantTimeZone() == null) {
                ext.setMerchantTimeZone(config.getMerchantTimeZone());
            }
        }
    }

    private byte[] calcHmac(String encodedKey, List<String> params) {
        byte[] sign;
        try {
            byte[] key = DatatypeConverter.parseHexBinary(encodedKey);

            // формируем MAC-подпись
            Mac mac = Mac.getInstance(MAC_ALG);
            SecretKeySpec signingKey = new SecretKeySpec(key, MAC_ALG);
            mac.init(signingKey);

            String strToSign = "";
            for (String param : params) {
                param = coalesce(param, "");
                strToSign += param.length() + param;
            }

            LOG.debug("MAC for string '{}'", strToSign);

            sign = mac.doFinal(strToSign.getBytes());
            // проставляем подпись в HEX-кодировке
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
        return sign;
    }

    private String takeBrand(String card) {
        if (card == null) {
            return null;
        }
        String firstChar = card.substring(0, 1);
        if ("4".equals(firstChar)) {
            return VISA_CODE;
        } else if ("5".equals(firstChar)) {
            return MASTERCARD_CODE;
        }
        return VISA_CODE;
    }

    private String takeTimestamp() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+0"));
        return formatter.format(now);
    }
}
