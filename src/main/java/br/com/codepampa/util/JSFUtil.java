package br.com.codepampa.util;


import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.util.PrettyURLBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Messages;

import javax.ejb.EJBException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSFUtil {

 public static String geraUrlRedirect(String mappingId, Object... parameters) {

     PrettyContext context = PrettyContext.getCurrentInstance(FacesContext
             .getCurrentInstance());
     PrettyURLBuilder builder = new PrettyURLBuilder();
     UrlMapping mapping = context.getConfig().getMappingById(mappingId);
     String targetURL = getContextPath()
             + builder.build(mapping, true, parameters);
     return targetURL;
 }

    /**
     * Redireciona para uma página específica. Deve ser passado como parametro o
     * ID da configuração pretty (url-mapping) da página e os parametros, sempre
     * em pares de chave e valor, por exemplo:
     * <br/>
     * <br/>
     * <p/>
     * <code>
     * JSFUtil.redirect("pagina-edit", "id", 13, "cat", "livros");
     * </code>
     * <p/>
     *
     * @param idPretty   String
     * @param parameters Enumerator de alvos, em pares, como chave/valor dos
     *                   parametros da url
     */
    public static void prettyRedirect(String idPretty, Object... parameters) {
        ExternalContext extContext = FacesContext.getCurrentInstance()
                .getExternalContext();

        try {
            String url = geraUrlRedirect(idPretty, parameters);
            extContext.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(JSFUtil.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalWarn("Problemas no redirecionamento: " + ex.getMessage());
        }
    }

    public static void redirect(String url) {
        ExternalContext extContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        try {
            extContext.redirect(url);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext()
                .getRequestContextPath();
    }

    public static String getURL() {
        ExternalContext extContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extContext
                .getRequest();
        PrettyContext context = PrettyContext.getCurrentInstance(request);
        return context.getRequestURL().toURL();
    }

    public static String inferMimeType(String fileExtension) {
        if ("jpg".equalsIgnoreCase(fileExtension)
                || "jpeg".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        } else if ("gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        } else if ("bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        } else if ("png".equalsIgnoreCase(fileExtension)) {
            return "image/png";
        } else if ("tif".equalsIgnoreCase(fileExtension)
                || "tiff".equalsIgnoreCase(fileExtension)) {
            return "image/tiff";
        }
        return "image/jpeg";
    }

    public static String getEnderecoIp() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest()).getRemoteAddr();
    }

    public static boolean isSessionTimedout() {
        final ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        final HttpSession session = (HttpSession) externalContext.getSession(false);
        final boolean newSession = (session == null) || (session.isNew());
        final boolean postback = !externalContext.getRequestParameterMap().isEmpty();
        final boolean timedout = postback && newSession;

        return timedout;
    }

    private static String getUserAgent() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getHeader("User-Agent").toLowerCase();
    }

    public static boolean isMobile() {
        String ua = JSFUtil.getUserAgent();
        if (ua.matches("(?i).*(android.+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|meego.+mobile|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*") || ua.substring(0, 4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
            return true;
        }
        return false;
    }

    public static DecimalFormat getDefaultDecimalPercentFormatter() {
        return getDecimalFormatter("#,##0.##'%'");
    }

    public static DecimalFormat getDefaultDecimalFormatter() {
        return getDecimalFormatter("#,##0.##");
    }

    public static DecimalFormat getDecimalFormatter(String pattern) {
        final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        return new DecimalFormat(pattern, symbols);
    }

    public static Locale getLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public static boolean prettyConstraintException(EJBException e, Constraintable constraintable) {
        if(e.getCause() instanceof PersistenceException && e.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cause = (ConstraintViolationException) e.getCause().getCause();
            for (ConstraintableNameBundle constraintableNameBundle : constraintable.getConstraints()) {
                if (cause.getConstraintName().equals(constraintableNameBundle.getConstraintName())) {
                    Messages.addGlobalWarn(Bundles.getString(constraintableNameBundle.getConstraintBundle()));
                    return true;
                }
            }
        }

        return false;
    }
}
