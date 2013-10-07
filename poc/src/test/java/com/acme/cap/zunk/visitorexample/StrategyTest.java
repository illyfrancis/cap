package com.acme.cap.zunk.visitorexample;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.acme.cap.zunk.visitorexample.message.Cash;
import com.acme.cap.zunk.visitorexample.message.Message;
import com.acme.cap.zunk.visitorexample.message.Portfolio;
import com.acme.cap.zunk.visitorexample.message.Share;
import com.acme.cap.zunk.visitorexample.strategy.InstanceOfStrategy;
import com.acme.cap.zunk.visitorexample.strategy.VisitingStrategy;
import com.acme.cap.zunk.visitorexample.strategy.WithVisitorStrategy;

public class StrategyTest {

    @Test
    public void testInstanceOfMerge() {
        Message cash = Cash.of("new cash");
        Message share = Share.of("new share");
        Portfolio portfolio = Portfolio.of("old cash", "old share");

        // instance of
        InstanceOfStrategy instanceOfStrategy = new InstanceOfStrategy();

        Portfolio merged = instanceOfStrategy.merge(portfolio, cash);

        assertThat(merged, not(sameInstance(portfolio)));
        assertThat(merged.getCash(), is("new cash"));
        assertThat(merged.getShare(), is("old share"));

        Portfolio mergedWithShare = instanceOfStrategy.merge(merged, share);
        assertThat(mergedWithShare, not(sameInstance(merged)));
        assertThat(mergedWithShare.getCash(), is("new cash"));
        assertThat(mergedWithShare.getShare(), is("new share"));
    }

    @Test
    public void testVisitorMerge() {
        Message cash = Cash.of("new cash");
        Message share = Share.of("new share");
        Portfolio portfolio = Portfolio.of("old cash", "old share");

        // with visitor
        WithVisitorStrategy withVisitorStrategy = new WithVisitorStrategy();

        Portfolio merged = withVisitorStrategy.merge(portfolio, cash);

        assertThat(merged, not(sameInstance(portfolio)));
        assertThat(merged.getCash(), is("new cash"));
        assertThat(merged.getShare(), is("old share"));

        Portfolio mergedWithShare = withVisitorStrategy.merge(merged, share);
        assertThat(mergedWithShare, not(sameInstance(merged)));
        assertThat(mergedWithShare.getCash(), is("new cash"));
        assertThat(mergedWithShare.getShare(), is("new share"));
    }
    
    @Test
    public void testVisitingStrategyMerge() {
        Message cash = Cash.of("new cash");
        Message share = Share.of("new share");
        Portfolio portfolio = Portfolio.of("old cash", "old share");

        // with visiting strategy
        VisitingStrategy visitingStrategy = new VisitingStrategy();

        Portfolio merged = visitingStrategy.merge(portfolio, cash);

        assertThat(merged, not(sameInstance(portfolio)));
        assertThat(merged.getCash(), is("new cash"));
        assertThat(merged.getShare(), is("old share"));

        Portfolio mergedWithShare = visitingStrategy.merge(merged, share);
        assertThat(mergedWithShare, not(sameInstance(merged)));
        assertThat(mergedWithShare.getCash(), is("new cash"));
        assertThat(mergedWithShare.getShare(), is("new share"));
    }
}
