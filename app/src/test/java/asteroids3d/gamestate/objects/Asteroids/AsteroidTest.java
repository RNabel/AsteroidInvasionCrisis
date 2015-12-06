package asteroids3d.gamestate.objects.Asteroids;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.w3c.dom.Text;


import java.util.TreeSet;

import edu.wlu.cs.levy.CG.KDTree;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Material.class, Texture.class})
public class AsteroidTest {

    Asteroid asteroid;
    AsteroidManager asteroidManager;
    KDTree kdTree;

    @Before
    public void setUp() throws Exception {
        Material mat = Mockito.mock(Material.class);
        PowerMockito.whenNew(Material.class).withAnyArguments().thenReturn(mat);
        Texture tex = Mockito.mock(Texture.class);
        PowerMockito.whenNew(Texture.class).withAnyArguments().thenReturn(tex);

        TreeSet<Double> rTimes = new TreeSet<>();
        rTimes.add(10d); rTimes.add(20d);
//        RajawaliScene scene = new RajawaliScene(null);
        asteroidManager = new AsteroidManager(rTimes, null);

        Vector3 location = new Vector3(0, 1, 1);
        Vector3 acceleration = new Vector3(1,1,1);
        Vector3 velocity = new Vector3(2,1,0);

        asteroid = new Asteroid(asteroidManager, location, acceleration, velocity, 10);

        kdTree = asteroidManager.getAsteroidLocationMap();
    }

    @Test
    public void testConstructor() throws Exception {
        // Ensure size of KDTree is 1.
        assertTrue(kdTree.size() == 1);
    }

    @Test
    public void testUpdatePosition() throws Exception {

        fail("Failed test.");
    }
}