package twitter4j;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TwitterStreamBuilder extends Configuration<TwitterStream, TwitterStreamBuilder> {
    private static final long serialVersionUID = -7194823238000676626L;

    TwitterStreamBuilder() {
        super(TwitterStreamImpl::new);

    }

    final List<ConnectionLifeCycleListener> connectionLifeCycleListeners = new ArrayList<>();

    final List<StreamListener> streamListeners = new ArrayList<>();
    final List<RawStreamListener> rawStreamListeners = new ArrayList<>();

    public TwitterStreamBuilder connectionLifeCycleListener(@NotNull ConnectionLifeCycleListener listener) {
        this.connectionLifeCycleListeners.add(listener);
        return this;
    }


    public TwitterStreamBuilder listener(@NotNull StreamListener streamListener) {
        this.streamListeners.add(streamListener);
        return this;
    }

    public TwitterStreamBuilder listener(@NotNull RawStreamListener rawStreamListener) {
        this.rawStreamListeners.add(rawStreamListener);
        return this;
    }


    public TwitterStreamBuilder onStatus(@NotNull Consumer<Status> onStatus) {
        this.streamListeners.add(new StatusAdapter(){
            @Override
            public void onStatus(Status status) {
                onStatus.accept(status);
            }

        });
        return this;
    }

    public TwitterStreamBuilder onException(Consumer<Exception> onException) {
        this.streamListeners.add(new StatusAdapter(){
            @Override
            public void onException(Exception ex) {
                onException.accept(ex);
            }

        });
        return this;
    }

}
