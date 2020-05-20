package com.esea.model;

public class Transaction {
    private Fx fx;
    private CustomerInfo customerInfo;
    private Acquirer acquirer;
    private Merchant merchant;
    private MerchantTransaction merchantTransaction;

    public Fx getFx() {
        return fx;
    }

    public void setFx(Fx fx) {
        this.fx = fx;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public Acquirer getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(Acquirer acquirer) {
        this.acquirer = acquirer;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public MerchantTransaction getMerchantTransaction() {
        return merchantTransaction;
    }

    public void setMerchantTransaction(MerchantTransaction merchantTransaction) {
        this.merchantTransaction = merchantTransaction;
    }
}
