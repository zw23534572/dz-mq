package com.dazong.reactor;

import org.junit.Test;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

public class ReactorTest {

    @Test
    public void test1(){
        Environment.initializeIfEmpty().assignErrorJournal();
        EventBus bus = EventBus.create(Environment.get());

        bus.on(Selectors.object("topic"), new Consumer<Event<?>>() {
            @Override
            public void accept(Event<?> event) {
                System.out.println("----" + event.getData());
            }
        });

        bus.notify("topic", Event.wrap("Hello World!"));
        System.out.println("000000");
    }
}
