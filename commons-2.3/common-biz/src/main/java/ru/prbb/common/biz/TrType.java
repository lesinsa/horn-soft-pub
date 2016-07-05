package ru.prbb.common.biz;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lesinsa on 10.07.2015.
 */
public enum TrType {


    /**
     * Операция списания в пользу merchant (TRTYPE = 1).
     */
    WRITE_OFF("1", (input) -> {
        P2PService.LOG.debug("Params list for signing: NONCE='{}', AMOUNT='{}', ORDER='{}', TIMESTAMP='{}', " +
                        "TRTYPE='{}', TERMINAL='{}'",
                input.getNonce(), input.getAmount(), input.getOrder(), input.getTimestamp(), input.getTrType(),
                input.getTerminal());
        ArrayList<String> params = new ArrayList<>();
        params.add(input.getNonce());
        params.add(input.getAmount());
        params.add(input.getOrder());
        params.add(input.getTimestamp());
        params.add(input.getTrType());
        params.add(input.getTerminal());
        return params;
    }, false),

    /**
     * Операция "P2P-переводы между картами" (TRTYPE = 8).
     */
    P2P_TANSFER("8", (input) -> {
        P2PService.LOG.debug("Params list for signing: NONCE='{}', AMOUNT='{}', ORDER='{}', TIMESTAMP='{}', TRTYPE='{}', " +
                        "TERMINAL='{}', PASSWORD='{}', CARD='{}', CVC2='{}', CVC2_RC='{}', PAYMENT_TO='{}'",
                input.getNonce(), input.getAmount(), input.getOrder(), input.getTimestamp(), input.getTrType(),
                input.getTerminal(), input.getPassword(), input.getCard(), input.getCvc2(), input.getCvc2rc(),
                input.getPaymentTo());
        ArrayList<String> params = new ArrayList<>();
        params.add(input.getNonce());
        params.add(input.getAmount());
        params.add(input.getOrder());
        params.add(input.getTimestamp());
        params.add(input.getTrType());
        params.add(input.getTerminal());
        params.add(input.getPassword());
        params.add(input.getCard());
        params.add(input.getCvc2());
        params.add(input.getCvc2rc());
        params.add(input.getPaymentTo());
        return params;
    }, true),;

    private final String code;
    private final ParamProvider paramProvider;
    private final boolean cvc2rc;


    TrType(String code, ParamProvider paramProvider, boolean cvc2rc) {
        this.code = code;
        this.paramProvider = paramProvider;
        this.cvc2rc = cvc2rc;
    }

    public String getCode() {
        return code;
    }

    public List<String> getParamList(P2PParams input) {
        return paramProvider.getParams(input);
    }

    public boolean isCvc2rc() {
        return cvc2rc;
    }
}
