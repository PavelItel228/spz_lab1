public class Main
{
	public static void main(String[] args)
	{
		Controller.createPhysicalMemory();
		Controller controller = new Controller();
		controller.start();
	}
}
