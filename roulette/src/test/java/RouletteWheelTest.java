import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Set;
import java.util.HashSet;

public class RouletteWheelTest {

    @Test
    public void shouldNotifyStopped20SecondsAfterSpin() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        RouletteWheel wheel = new RouletteWheel(wheelObserver);

        wheel.spin(20000);
        wheel.tick(20000);

        verify(wheelObserver).stopped(anyInt());
    }


    @Test
    public void shouldProvideRandomBallLocationWhenStopped() {
        final boolean seenAll[] = new boolean[1];
        seenAll[0] = false;

        WheelObserver wheelObserver = new WheelObserver() {
            Set<Integer> seen = new HashSet<Integer>();
            public void stopped(final int location) {
                if (location < 0 || location > 36)
                    throw new IllegalArgumentException();
                seen.add(location);
                if (seen.size() == 37) seenAll[0] = true;
            }
        };
        RouletteWheel wheel = new RouletteWheel(wheelObserver);

        for (int x = 0; x < 1000; x++)
        {
            wheel.spin(0);
            wheel.tick(20000);
        }

        assertTrue(seenAll[0]);
    }

    @Test
    public void shouldSpecifyBallLocationWhenStopped() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        RouletteWheel wheel = new RouletteWheel(wheelObserver);


        long spinFor20s = 20000;
        wheel.spin(spinFor20s);
        wheel.tick(20000);

        verify(wheelObserver, times(1)).stopped(anyInt());
    }

    @Test
    public void shouldSpecifyBallLocationOnceWhenStopped() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        RouletteWheel wheel = new RouletteWheel(wheelObserver);

        long spinFor20s = 20000;
        wheel.spin(spinFor20s);
        wheel.tick(20000);
        wheel.tick(20001);

        verify(wheelObserver, times(1)).stopped(anyInt());
    }


    @Test
    public void shouldNotNotifyStoppedBeforeSpinEnd() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        RouletteWheel wheel = new RouletteWheel(wheelObserver);

        long spinFor20s = 20000;
        wheel.spin(spinFor20s);

        long timeEndMs = 10000;
        wheel.tick(timeEndMs);

        verify(wheelObserver, never() ).stopped(anyInt());
    }
}