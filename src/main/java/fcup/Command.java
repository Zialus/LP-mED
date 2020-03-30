package fcup;

import lombok.Data;

@Data
public class Command {
    public final Type tipo;
    public final Cursor cursor;
    public final char caracter;
}
