package no.kantega.ldd;

import java.util.Base64;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Client client = ClientBuilder.newClient();
    private static final WebTarget webTarget = client.target("https://log-workshop-server.herokuapp.com");

    private static final Logger log = LoggerFactory.getLogger(Main.class);


    public static class Fnr
    {
        public final String value;
        public Fnr(String value) { this.value= value; }

        @Override
        public String toString() { return value.substring(0,6)+"*****"; }
    }

    public static void main(
        final String[] args) 
    {
        log.info("Starter problemkundesjekk");
        final String token = login();
        while (true) {
            for (String fnr : finnPersoner()) {
                try {
                    PersonInfo person = hentPerson(token, fnr);
                    long gjeldsgrad = 5 * person.inntekt - person.gjeld;
                    if (gjeldsgrad < 0) {
                        //System.out.println("Problemkunde: " + person.navn + " - " + person.gjeld);
                        log.info("Problemkunde: {} - {}", person.navn, person.gjeld);
                    }
                } catch (Exception e) {
                    log.error("Failed to check fnr: {}, error: {}", new Fnr(fnr), e.getMessage());
                }
            }
        }
    }

    public static String login()
    {
        final String user = System.getProperty("user", "kantega");
        final String password = System.getProperty("password");
        if (password == null) {
            log.error("Password not specified!");
        }
        return Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
    }

    public static PersonInfo hentPerson(
        final String token,
        final String fnr)
    {
        WebTarget withParam = webTarget.queryParam("q", fnr);
        Builder invocationBuilder = withParam.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("Authorization", "Basic " + token);
        Response response = invocationBuilder.get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Noe gikk galt: " + response.readEntity(String.class));
        }
        return response.readEntity(PersonInfo.class);
    }

    public static String[] finnPersoner()
    {
        return new String[] {
            "12030048288",
            "09091298760",
            "07030824157",
            "17129476442",
            "04041223401",
            "16061987130",
            "05036783010",
            "17025932230",
            "31019995914",
            "25077373142"
        };        
    }
}
