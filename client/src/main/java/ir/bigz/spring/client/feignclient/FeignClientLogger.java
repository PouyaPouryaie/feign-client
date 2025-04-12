package ir.bigz.spring.client.feignclient;

import feign.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignClientLogger extends Logger {

    org.slf4j.Logger logger = LoggerFactory.getLogger(FeignClientLogger.class);

    @Override
    protected void log(String configKey, String template, Object... objects) {
        logger.info(String.format(methodTag(configKey) + template, objects));
    }
}
