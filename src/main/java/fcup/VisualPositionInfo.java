package fcup;

import lombok.Data;

@Data
public class VisualPositionInfo {
    final int startingLine;
    final int numberOfLines;
    final int lastColumn;
}
