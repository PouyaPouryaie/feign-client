package ir.bigz.spring.server;

import lombok.*;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class ServerHeaderHolder {

    private String xClientRequester;
    private String xClientUUID;
}
