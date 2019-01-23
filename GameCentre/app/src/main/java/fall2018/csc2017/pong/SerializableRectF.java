package fall2018.csc2017.pong;
import android.graphics.RectF;

import java.io.IOException;
import java.io.Serializable;

/**
 * RectF that is Serializable
 * Excluded from unit test since this is just a Model
 */
public class SerializableRectF extends RectF implements Serializable {

    private static final long serialVersionUID = 1L;

    private RectF mRect;

    SerializableRectF(RectF rect) {
        this.mRect = rect;
        this.mRect.left = rect.left;
        this.mRect.top = rect.top;
        this.mRect.right = rect.right;
        this.mRect.bottom = rect.bottom;
    }

    RectF getRectF() {
        return this.mRect;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        float left = mRect.left;
        float top = mRect.top;
        float right = mRect.right;
        float bottom = mRect.bottom;

        out.writeFloat(left);
        out.writeFloat(top);
        out.writeFloat(right);
        out.writeFloat(bottom);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        float left = in.readFloat();
        float top = in.readFloat();
        float right = in.readFloat();
        float bottom = in.readFloat();

        mRect = new RectF(left, top, right, bottom);
    }
}