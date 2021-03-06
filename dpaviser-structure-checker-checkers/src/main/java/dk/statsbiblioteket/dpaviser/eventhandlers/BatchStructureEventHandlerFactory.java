package dk.statsbiblioteket.dpaviser.eventhandlers;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.newspaper.schematron.XmlBuilderEventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Provides the complete set of structure checkers for the batch structure.
 */
public class BatchStructureEventHandlerFactory implements EventHandlerFactory {

    private final ResultCollector resultCollector;
    private final Properties properties;


    public BatchStructureEventHandlerFactory(Properties properties, ResultCollector resultCollector) {
        this.properties = properties;
        this.resultCollector = resultCollector;
    }

    @Override
    public List<TreeEventHandler> createEventHandlers() {
        final List<TreeEventHandler> eventHandlers = new ArrayList<>();
        /*
        TreeNodeState nodeState = new TreeNodeState();
        eventHandlers.add(nodeState); // Must be the first eventhandler to ensure a update state used by the following handlers (a bit fragile).
        */
        eventHandlers.add(new XmlBuilderEventHandler());
        eventHandlers.add(new PageSequenceChecker(resultCollector));
        // ... and possibly more.
        return eventHandlers;
    }
}
