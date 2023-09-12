package com.ridelo.management.notificationService;

import com.ridelo.management.entities.Driver;
import com.ridelo.management.model.EmailInfo;

import java.util.HashMap;
import java.util.Map;

public class EmailDataConstants {

    public static final String BASE_URL = "http://coded-appli-11hjssge31otk-987020417.ap-south-1.elb.amazonaws.com:8080/Ridelo/";
//    public static final String BASE_URL = "http://localhost:8080/";

    public enum MessageType{
        EMAIL_VERIFICATION,
        PROFILE_UPDATE,
        DOCUMENT_VERIFICATION
    }

    private static String emailVerificationBody =
            "Dear %s,<br>"
                    + "Please click the link below to verify your email registration:<br>"
                    + "<h3><a href=\""+BASE_URL+"email/verification/%s\" >VERIFY</a></h3>"
                    + "<br><br>Thank you,<br>"
                    + "Ridelo.com Team<br><br>"
            + "NOTE: Please do not reply to this message. Replies are routed to a mailbox that is not monitored.";

    private static String documentVerificationBody =
            "Dear %s,<br>"
                    + "Your document verification status is updated:<br>"
                    + "<h3>Document Verification : %s</h3>"
                    + "<br>Your device will be delivered at your registered address by our delivery partner " +
                    "post your document verification: <br>"
                    + "<h3>%s</h3> <br><br>"
                    + "Thank you,<br>"
                    + "Ridelo.com Team<br><br>"
                    + "NOTE: Please do not reply to this message. Replies are routed to a mailbox that is not monitored.";

    private static String profileInfoUpdateBody =
            "Dear %s,<br>"
                    + "You profile information has been updated successfully.<br>"
                    + "Please find the update profile information below:<br>"
                    + "%s"
                    + "<br><br>Thank you,<br>"
                    + "Ridelo.com Team<br><br>"
                    + "NOTE: Please do not reply to this message. Replies are routed to a mailbox that is not monitored.";


    private EmailDataConstants(){}
    public static Map<String, EmailInfo> emailInfoMap;
    static {
        emailInfoMap = new HashMap<>();
        emailInfoMap.put(MessageType.EMAIL_VERIFICATION.toString(),EmailInfo.builder()
                .subject("RIDELO: Email Verification...")
                .body(emailVerificationBody).build());
        emailInfoMap.put(MessageType.PROFILE_UPDATE.toString(),EmailInfo.builder()
                .subject("RIDELO: Profile update...")
                .body(profileInfoUpdateBody).build());
        emailInfoMap.put(MessageType.DOCUMENT_VERIFICATION.toString(),EmailInfo.builder()
                .subject("RIDELO: Document Verification Update")
                .body(documentVerificationBody).build());

    }

    public static String buildDriverData(Driver driver){
        return "<br>Name= " + driver.getName() +"<br>" +
                "Email= " + driver.getEmail() + "<br>" +
                "Contact= " + driver.getContact() + "<br>" +
                "DateOfBirth= " + driver.getDateOfBirth() + "<br>" +
                "Address= " + driver.getAddress() + "<br>" +
                "UpdatedAt= " + driver.getUpdatedAt();
    }
}
