package ihm.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.owlike.genson.Genson;
import biz.factory.BizFactory;
import biz.ucc.UserUcc;
import biz.user.UserDto;
import util.AppContext;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

public class UtilServiceImpl implements UtilService {

  @DependanceInjection
  private transient AppContext appContext;
  @DependanceInjection
  private transient UserUcc userUcc;
  @DependanceInjection
  private transient BizFactory factory;
  @DependanceInjection
  private MonLogger monLogger;

  private Genson genson = new Genson();

  @Override
  public UserDto getCurrentUser(HttpServletRequest req) {

    UserDto whoami = (UserDto) req.getSession().getAttribute("userDb");
    if (whoami != null) {
      return whoami;
    }
    String token = null;

    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName()) && cookie.getSecure()) {
          token = cookie.getValue();
        } else if ("token".equals(cookie.getName()) && token == null) {
          token = cookie.getValue();
        }
      }
    }
    if (token == null) {
      return null;
    }

    try {
      Map<String, Object> decodedPayload =
          new JWTVerifier(appContext.getValueProp("JWTSecret")).verify(token);
      /**
       * stocké dans token idealement: date expiration id qui a distribue le token
       */
      if (!req.getRemoteAddr().equals(decodedPayload.get("ip"))) {
        monLogger.getMonLog().finer(
            "Accès utilisateur bloqué : adresse cookie ne correspond pas à l'adresse distante."
                + " ip distant : " + req.getRemoteAddr() + ", ip cookie : "
                + decodedPayload.get("ip"));
        return null;
      }

      whoami = userUcc.trouverUtilisateurById((int) decodedPayload.get("idUtilisateur"));

      if (whoami != null) {
        req.getSession().setAttribute("userDb", whoami);
      }
    } catch (Exception ex) {
      monLogger.getMonLog().finer(
          "Problème token, ip : " + req.getRemoteAddr() + " - " + Util.stackTraceToString(ex));
    }
    return whoami;
  }

  @Override
  public void logout(HttpServletRequest req, HttpServletResponse resp) {

    req.getSession().invalidate();
    Cookie cookie = new Cookie("token", "");
    cookie.setPath("/");
    cookie.setMaxAge(0);
    resp.addCookie(cookie);
  }

  @Override
  public void startSession(UserDto user, HttpServletRequest req, HttpServletResponse resp) {

    req.getSession().setAttribute("userDb", user);

    Map<String, Object> claims = new HashMap<>();
    claims.put("idUtilisateur", user.getIdUtilisateur());
    claims.put("ip", req.getRemoteAddr());

    String secret = appContext.getValueProp("JWTSecret");

    String token = new JWTSigner(secret).sign(claims);

    Cookie cookie = new Cookie("token", token);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 365);
    resp.addCookie(cookie);
  }

  @Override
  public void renvoyerCodeErr(HttpServletResponse resp, Exception ex, int code) throws IOException {
    monLogger.getMonLog().warning("Erreur " + code + "\n\t" + Util.stackTraceToString(ex));
    Map<String, Object> infosRetour = new HashMap<String, Object>() {
      {
        put("message", ex.getMessage());
      }
    };

    resp.setStatus(code);
    resp.setContentType("application/json");
    resp.getOutputStream().write(genson.serialize(infosRetour).getBytes(Charset.forName("UTF-8")));
  }

  @Override
  public void renvoyerCodeErrEtMess(HttpServletResponse resp, String messageErreur, int code)
      throws IOException {
    Map<String, Object> infosRetour = new HashMap<String, Object>() {
      {
        put("message", messageErreur);
      }
    };
    monLogger.getMonLog().warning("Erreur " + code + " - " + messageErreur);
    resp.setStatus(code);
    resp.setContentType("application/json");
    resp.getOutputStream().write(genson.serialize(infosRetour).getBytes(Charset.forName("UTF-8")));
  }

  @Override
  public void renvoyerCodeErrEtMessEtObj(HttpServletResponse resp, Exception ex, int code,
      Object object, Genson gens) throws IOException {

    Map<String, Object> infosRetour = new HashMap<String, Object>() {
      {
        put("message", ex.getMessage());
        put("objetDb", object);
      }
    };
    monLogger.getMonLog()
        .warning("Erreur " + code + ", object : " + object + "\n\t" + Util.stackTraceToString(ex));
    resp.setStatus(code);
    resp.setContentType("application/json");
    resp.getOutputStream().write(gens.serialize(infosRetour).getBytes(Charset.forName("UTF-8")));
    return;
  }

  @Override
  public boolean verifStud(HttpServletRequest req, HttpServletResponse resp, int idStudentFrontEnd)
      throws IOException {
    if (idStudentFrontEnd != ((UserDto) req.getAttribute("currentUser")).getIdUtilisateur()) {
      renvoyerCodeErrEtMess(resp, "Action Interdite", 401);
      return false;
    }
    return true;
  }

  @Override
  public void whoami(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("application/json");
    resp.getOutputStream()
        .write(genson.serialize(getCurrentUser(req)).getBytes(Charset.forName("UTF-8")));
  }

}
