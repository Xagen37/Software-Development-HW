package impls;

import apis.DrawingApi;

import java.util.function.Function;

public interface Factorizable {
    public static Function<DrawingApi, GraphImpl> getFactory() {
        return null;
    }
}
