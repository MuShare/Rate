package edu.ut.softlab.rate.component;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by alex on 16-8-9.
 */

@Component("serverConfig")
public class ServerConfig implements ServletContextAware {
    private String serverRootUrl;
    public String getServerRootUrl(){ return serverRootUrl; }
    public void setServletContext(ServletContext servletContext){
        this.serverRootUrl=servletContext.getRealPath("/");
    }
}