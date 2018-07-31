package ru.alx.edx.ai;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Game implements ApplicationListener, InputProcessor {
    private Camera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private Texture texture;

    private Field field;
    private final int CELL_SIZE = 64;
    private TextureRegion textureRegion;
    private Sprite pacman;

    @Override
	public void create() {
        Gdx.graphics.setWindowedMode(800, 600);

        field = new Field(10, 10);

        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.local("/images/sprites.png"));
        textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, CELL_SIZE, CELL_SIZE);

        pacman = new Sprite(new TextureRegion(texture, 0, CELL_SIZE, CELL_SIZE, CELL_SIZE));

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(width, height);
        camera.translate(320, 240, 0);
        viewport = new ScreenViewport(camera);
        Gdx.input.setInputProcessor(this);
	}

	@Override
	public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
//        camera.translate(width / 2, height / 2, 0);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

        batch.begin();
        field.iterate()
                .filter(Objects::nonNull)
                .forEach(cell -> {
                    int offset = cell.getCellType();
                    textureRegion.setRegion(offset * CELL_SIZE, 0, CELL_SIZE, CELL_SIZE);

                    Sprite s = new Sprite(textureRegion);

                    s.setPosition(cell.x * 64, cell.y * 64);
                    s.draw(batch);
                });

        pacman.setPosition(field.pacman.x * CELL_SIZE, field.pacman.y * CELL_SIZE);
        pacman.draw(batch);

		batch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}


	private boolean ctrl = false;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.F) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            Vector3 tp = new Vector3();
            camera.unproject(tp.set(x, y, 0));
            field.setPacman((int)(tp.x / CELL_SIZE), (int)(tp.y / CELL_SIZE));
        }
        else if (keycode == Input.Keys.S && ctrl) {
            field.save();
        }
        else if (keycode == Input.Keys.O && ctrl) {
            field = Field.load(Paths.get("pacman.map"));
        }
        else if (Input.Keys.CONTROL_LEFT == keycode || Input.Keys.CONTROL_RIGHT == keycode) {
            ctrl = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                ctrl = false;
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;

        Vector3 tp = new Vector3();
        camera.unproject(tp.set(screenX, screenY, 0));

        if (tp.x > 0 && tp.x < field.getWidth() * CELL_SIZE &&
            tp.y > 0 && tp.y < field.getHeight() * CELL_SIZE)
        {
            int x = (int)(tp.x / CELL_SIZE);
            int y = (int)(tp.y / CELL_SIZE);
            field.addCell(x, y);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
