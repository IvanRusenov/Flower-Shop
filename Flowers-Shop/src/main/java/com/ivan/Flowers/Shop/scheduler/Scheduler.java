package com.ivan.Flowers.Shop.scheduler;

import com.ivan.Flowers.Shop.enums.StatusType;
import com.ivan.Flowers.Shop.services.impls.OrderServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final OrderServiceImpl orderService;

    public Scheduler(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "1 * * * * *")
    public void deleteOrdersWithStatusCanceled(){

        orderService.getAllOrdersDesc().stream()
               .filter(o -> o.getStatus().equals(StatusType.CANCELED))
               .forEach(o -> orderService.delete(o.getId()));
        System.out.println("DELETING CANCELED ORDERS");
    }
}
