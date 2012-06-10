package swingdemo.framework;

/**
 * A listener for a particular event, needs parameterization for typesafe usage of the payload.
 *
 * @author Christian Bauer
 */
public interface DefaultEventListener<PAYLOAD> {
    public void handleEvent(DefaultEvent<PAYLOAD> event);
}
