package com.guiji.yygh.hosp.receiver;

import com.guiji.model.hosp.Schedule;
import com.guiji.rabbit.constant.MqConst;
import com.guiji.rabbit.service.RabbitService;
import com.guiji.vo.msm.MsmVo;
import com.guiji.vo.order.OrderMqVo;
import com.guiji.yygh.hosp.service.ScheduleService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HospitalReceiver {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        Schedule schedule = scheduleService.getScheduleId(orderMqVo.getScheduleId());
        if(null != orderMqVo.getAvailableNumber()) {
            //下单成功更新预约数
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        } else {
            //取消预约更新预约数
            int availableNumber = schedule.getAvailableNumber() + 1;
            schedule.setAvailableNumber(availableNumber);
        }
        scheduleService.update(schedule);

        //发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }

}