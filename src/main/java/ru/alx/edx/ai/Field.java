package ru.alx.edx.ai;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Field {
    private final int width;
    private final int height;

    private Cell[] cells;

    public final Pacman pacman = new Pacman();
    private List<Ghost> ghost = new ArrayList<>();

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width * height];
    }

    public void setPacman(int x, int y) {
        System.out.println(x + " " + y);
        pacman.setPosition(x, y);
    }

    public Cell addCell(int x, int y) {
        Cell cell = cells[y * width + x];
        if (cell != null) {
            cells[y * width + x] = null;
            if (cell.down != null) {
                cell.down.up = null;
                cell.down.refresh();
            }
            if (cell.up != null) {
                cell.up.down = null;
                cell.up.refresh();
            }
            if (cell.left != null) {
                cell.left.right = null;
                cell.left.refresh();
            }
            if (cell.right != null) {
                cell.right.left = null;
                cell.right.refresh();
            }
        } else {
            cell = new Cell(x, y);
            cells[y * width + x] = cell;

            if (x > 0) {
                cell.left = cells[y * width + x - 1];
                if (cell.left != null) {
                    cell.left.right = cell;
                    cell.left.refresh();
                }
            }

            if (x < width - 1) {
                cell.right = cells[y * width + x + 1];
                if (cell.right != null) {
                    cell.right.left = cell;
                    cell.right.refresh();
                }
            }

            if (y > 0) {
                cell.down = cells[(y - 1) * width + x];
                if (cell.down != null) {
                    cell.down.up = cell;
                    cell.down.refresh();
                }
            }

            if (y < height - 1) {
                cell.up = cells[(y + 1) * width + x];
                if (cell.up != null) {
                    cell.up.down = cell;
                    cell.up.refresh();
                }
            }
        }

        cell.refresh();

        return cell;
    }

    Stream<Cell> iterate() {
        return Stream.of(cells);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void save() {
        System.out.println("save");

        try (OutputStream os = Files.newOutputStream(Paths.get("pacman.map"), StandardOpenOption.CREATE, StandardOpenOption.CREATE);
             ObjectOutputStream oos = new ObjectOutputStream(os))
        {
            oos.writeInt(width);
            oos.writeInt(height);

            oos.writeInt(pacman.x);
            oos.writeInt(pacman.y);

            oos.writeInt((int) iterate().filter(Objects::nonNull).count());

            iterate().filter(Objects::nonNull).forEach(cell -> {
                try {
                    oos.writeInt(cell.x);
                    oos.writeInt(cell.y);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Field load(Path path) {
        System.out.println("load");

        try (InputStream is = Files.newInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(is))
        {
            int width = ois.readInt();
            int height = ois.readInt();
            Field field = new Field(width, height);

            field.setPacman(ois.readInt(), ois.readInt());

            for (int i = 0; i < ois.readInt(); i++) {
                field.addCell(ois.readInt(), ois.readInt());
            }

            return field;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
