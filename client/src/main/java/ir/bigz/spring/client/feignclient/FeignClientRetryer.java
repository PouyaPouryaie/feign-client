package ir.bigz.spring.client.feignclient;

import feign.Feign;
import feign.RetryableException;
import feign.Retryer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FeignClientRetryer implements feign.Retryer {

    Logger logger = LoggerFactory.getLogger(FeignClientRetryer.class);

    private final int maxAttempts;
    private final long period;
    private final long maxPeriod;
    int attempt;
    long startTime = this.currentTimeMillis();

    public FeignClientRetryer(int maxAttempts, long period, long maxPeriod) {
        this.maxAttempts = maxAttempts;
        this.period = period;
        this.maxPeriod = maxPeriod;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {

        if(this.attempt++ > this.maxAttempts) {
            logger.error("ERROR: Exceeded Retrying after {} attempts!", this.maxAttempts);
            throw new FeignClientServerException(500,
                    "Failed to connect to the server. Please try again later.",
                    "90001");
        } else {
            long interval;
            if (e.retryAfter() != null && e.retryAfter() > 0) {
                interval = this.currentTimeMillis() - (startTime);

                if(e.retryAfter() > interval) {
                    interval = e.retryAfter();
                }

                if(interval > this.maxPeriod) {
                    interval = this.maxPeriod;
                }
            } else {
                interval = getNextInterval();
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw e;
            }
        }
    }

    protected long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    protected long getNextInterval() {
        long interval = (long) (this.period * Math.pow( 2.0F, (this.attempt-1)));
        return Math.min(interval, this.maxPeriod);
    }

    @Override
    public Retryer clone() {
        return new FeignClientRetryer(maxAttempts, period, maxPeriod);
    }
}
