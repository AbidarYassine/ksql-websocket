package ma.octo.demoksqlwebsocket.config;


import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KsqlDbConfig {

    @Value("${ksql.host}")
    private String host;
    @Value("${ksql.port}")
    private String port;

    @Bean
    public Client client() {
        ClientOptions options = ClientOptions.create().setHost(host).setPort(Integer.parseInt(port));
        return Client.create(options);
    }
}
