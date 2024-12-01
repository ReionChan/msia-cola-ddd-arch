package io.github.reionchan.products.command;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.exception.BizException;
import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.products.assembler.StockAssembler;
import io.github.reionchan.products.convertor.MessageConvertor;
import io.github.reionchan.products.dto.StockDTO;
import io.github.reionchan.products.model.entity.Stock;
import io.github.reionchan.products.repository.IStockRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.github.reionchan.products.consts.RabbitMQConst.STOCK_SUB_EXCHANGE;
import static io.github.reionchan.products.consts.RabbitMQConst.STOCK_SUB_ROUTING_KEY;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class StockSubCmdExe {

    @Resource
    private IStockRepository stockRepository;
    @Resource
    private StockAssembler stockAssembler;
    @Resource
    private MessageConvertor messageConvertor;
    @Resource
    private MQManager mqManager;

    public List<StockDTO> execute(Long orderId, Set<StockDTO> stockDtoSet) {
        // 业务
        List<StockDTO> ret = new ArrayList<>();
        Set<Stock> outOfStockSet = stockRepository.subStock(stockDtoSet);
        if (outOfStockSet.isEmpty()) {
            try {
                MQMessage mqMessage = messageConvertor.subStockSuccess2Message(orderId);
                Assert.isTrue(mqManager.save(mqMessage), "库存扣减消息保存异常");
                mqManager.sendMessage(STOCK_SUB_EXCHANGE, STOCK_SUB_ROUTING_KEY, mqMessage, new CorrelationData(mqMessage.getMessageId()));
            } catch (Exception e) {
                throw new BizException("库存扣减发送消息异常", e);
            }
        } else {
            ret.addAll(stockAssembler.toDTOList(outOfStockSet));
        }
        return ret;
    }
}
