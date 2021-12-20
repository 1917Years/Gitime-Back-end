package capstone.gitime.domain.endpoint.service;

import capstone.gitime.domain.endpoint.entity.EndPoint;
import capstone.gitime.domain.endpoint.repository.EndPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EndPointInit {

    private final EndPointRepository endPointRepository;
    private final static Map<String, Boolean> portTree = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Integer port = 8090;
        for (int i = 0; i < 30; i++) {
            portTree.put(String.valueOf(port++), false);
        }
        endPointRepository.findAll()
                .forEach((item) -> {
                    if (item.getServerPort() != null) {
                        portTree.put(item.getServerPort(), true);
                    }
                });

        log.info("{}", portTree);
    }

    public Map<String,Boolean> callPortTree() {
        return portTree;
    }
}
