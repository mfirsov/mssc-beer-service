package guru.springframework.msscbeerservice.services.order;


import guru.sfg.brewery.model.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.ValidateOrderResult;
import guru.springframework.msscbeerservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BeerOrderValidationListener {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderValidator beerOrderValidator;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateBeerOrderRequest request) {
       Boolean isValid = beerOrderValidator.validate(request.getBeerOrderDto());

       jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT_QUEUE, ValidateOrderResult.builder()
               .isValid(isValid)
               .orderId(request.getBeerOrderDto().getId())
               .build());
    }

}
