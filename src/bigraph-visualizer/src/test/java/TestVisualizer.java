import edu.berkeley.eloi.bigvis.Visualizer;

public class TestVisualizer
{

    public TestVisualizer()
    {
    }

    public static void main(String args[])
        throws InterruptedException
    {
        Visualizer frame = new Visualizer(
            "airfield_Location.(u0_UAV[wifi]\n" +
                "                 | u1_UAV[wifi]\n" +
                "                 | cs0_ControlStation[wifi])\n" +
                "| harbour_Location.(vessel0_Vessel[x,ais].(drifter0_Drifter[ais]\n" +
                "                                         | drifter1_Drifter[ais]))\n" +
                "|" +
                "| searchArea_Location");
        frame.setVisible(true);

        Thread.sleep(10000);

        frame.update("l0_Location.uav0[x] | l1_Location.uav1[x]");
    }
}
