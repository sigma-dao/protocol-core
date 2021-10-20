package com.sigma.dao.error;

public final class ErrorCode {
    public static final String E0001 = "Disbursement frequency is a mandatory field.";
    public static final String E0002 = "Management fee is a mandatory field.";
    public static final String E0003 = "Minimum subscription is a mandatory field.";
    public static final String E0004 = "Performance fee is a mandatory field.";
    public static final String E0005 = "Redemption frequency is a mandatory field.";
    public static final String E0006 = "Subscription asset is a mandatory field.";
    public static final String E0007 = "Type is a mandatory field.";
    public static final String E0008 = "ID is a mandatory field.";
    public static final String E0009 = "Fund cannot be found with specified ID.";
    public static final String E0010 = "Fund can only be updated when status = PROPOSED";
    public static final String E0011 = "Fund activation date cannot be changed.";
    public static final String E0012 = "Activation date is a mandatory field.";
    public static final String E0013 = "Activation date is too soon.";
    public static final String E0014 = "Fund status cannot be changed.";
    public static final String E0015 = "Unsupported Tendermint transaction.";
    public static final String E0016 = "Unsupported Tendermint query.";
    public static final String E0017 = "Blockchain is a mandatory field.";
    public static final String E0018 = "Contract address is a mandatory field.";
    public static final String E0019 = "Symbol is a mandatory field.";
    public static final String E0020 = "Asset cannot be deleted when it is being used by a fund.";
    public static final String E0021 = "Asset cannot be found with specified ID.";
    public static final String E0022 = "The asset you're trying to add already exists.";
    public static final String E0023 = "The network config is missing.";
    public static final String E0024 = "The subscription asset must be approved by governance.";
    public static final String E0025 = "Governance action cannot be found with specified ID.";
    public static final String E0026 = "User not found with public key.";
    public static final String E0027 = "Signature is invalid.";
}