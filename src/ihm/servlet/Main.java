package ihm.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import exceptions.FatalException;
import util.AppContext;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

class Main {
  @DependanceInjection
  private AppContext appContext;
  @DependanceInjection
  private DispatcherServlet dispatcherServlet;
  @DependanceInjection
  private MonLogger monLogger;

  public static void main(String[] args) {
    String properties = null;

    if (args.length > 0) {
      properties = args[0];
    }
    AppContext appContext = new AppContext();
    appContext.loadProps(properties);
    Main main = new Main();
    appContext.recurDepInj(main);
    main.startServer();
  }

  /**
   * Démarre le serveur web.
   */
  private void startServer() {
    WebAppContext context = new WebAppContext();
    context.setContextPath("/");
    context.addServlet(new ServletHolder(dispatcherServlet), "/");
    context.setResourceBase("www");

    Server server = new Server(Integer.parseInt(appContext.getValueProp("port")));
    server.setHandler(context);

    try {
      server.start();
      monLogger.getMonLog().info("Serveur démarré");
    } catch (Exception ex) {
      monLogger.getMonLog()
          .severe("Impossible de démarrer le serveur\n\t" + Util.stackTraceToString(ex));
      throw new FatalException();
    }
  }
}
