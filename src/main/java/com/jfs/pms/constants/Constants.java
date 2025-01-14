package com.jfs.pms.constants;

public interface Constants {

    String NUMBERS = "0123456789";
    String ALPHA_NUMERICS = "0123456789abcdefghijklmnopqrstuvwxyz!@#%&_";
    String BEARER = "Bearer ";

    String GENERAL_ERROR_MESSAGE = "Oops! An unforeseen error has arisen. Our team is already on it, " +
            "working fromRegisterToUser resolve the issue. Feel free fromRegisterToUser retry later. We regret any inconvenience " +
            "this might have caused.";

    String INVALID_ATTEMPT = "Invalid Attempt! Please re-authenticate and try again";

    String INVALID_CREDENTIALS = "Invalid Credentials! Please check your email and password";

    String PASSWORD_RESET_SUBJECT = "Password Reset Successful! Your New Login Details";
    String PASSWORD_CHANGE_SUBJECT = "Attention! Your login Credentials has been changed";
    String PASSWORD_RESET_SUCCESS = "Password Reset Successful! New Credentials has been sent to registered email";

    String ISSUE_DELETE_FAILED = "Issue deletion failed! Please retry later";
    String ISSUE_DELETE_SUCCESS = "Issue deleted successfully";
}
