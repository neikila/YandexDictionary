package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by neikila on 14.10.15.
 */
public class ErrorReflection {

    public static void reflectOnError(Context context, ErrorTypes type) {
        String errorMessage;
        switch (type) {
            case SqlError:
                errorMessage = "Sorry. There is a error in application please reload application." +
                        "If it hasn't solved your problem, please, reinstall application.";
                break;
            case TranslationError:
                errorMessage = "Sorry. There is a error in application please reload application." +
                        "If it hasn't solved your problem, please, reinstall application.";
                break;
            case NoInternetCantUpdateRoutes:
                errorMessage = "No Internet connection. Can't update routes.";
                break;
            case NoInternet:
                errorMessage = "Word is not in database and there is no Internet connection";
                break;
            default:
                errorMessage = "Nice day, don't you think so?";
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }
}
