package me.ronygomes.identity_fort.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class WebHelper {

    private static final String ERROR_REDIRECT_MESSAGE_KEY = "errorRedirectMessage";
    private static final String SUCCESS_REDIRECT_MESSAGE_KEY = "successRedirectMessage";

    public static void putSuccessRedirectMessage(RedirectAttributes ra, String message) {
        ra.addFlashAttribute(SUCCESS_REDIRECT_MESSAGE_KEY, message);
    }

    public static void putErrorRedirectMessage(RedirectAttributes ra, String message) {
        ra.addFlashAttribute(ERROR_REDIRECT_MESSAGE_KEY, message);

    }
}
