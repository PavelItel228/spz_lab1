import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


public class Controller
{
	public static List<PhysicalPage> physicalPages;
	private List<Process> processes;
	public static Integer tick = 1;
	private Optional<Process> currentProccess = Optional.empty();
	private Integer proccessCreated = Constants.START_PROCESS_NUMBER;


	public Controller()
	{
		this.processes = createProcesses();
	}

	public static void createPhysicalMemory(){
		physicalPages = new ArrayList<>();
		for (int i = 0; i < Constants.PHYSICAL_MEMORY_SIZE; i++)
		{
			physicalPages.add(new PhysicalPage(Optional.empty()));
		}
	}

	public void start(){
		while(true)
		{
			if (isMemoryCheckTime())
			{
				processes.forEach(proccess -> proccess.getMmu().performMemoryCheck());
			}
			setCurrentProcess();
			generateNewProcess();
			currentProccess.ifPresentOrElse(Process::run, () -> System.out.println(String.format(Constants.NO_WORKING_PROCESSES, tick)));
			tick += 1;
			if (isFinishExecution())
			{
				//todo logs here
				return;
			}
		}
	}


	private boolean isMemoryCheckTime(){
		return tick % Constants.MEMORY_CHECK_TIME == 0;
	}

	private void setCurrentProcess() {
		if (isQuantFinished() || isCurrentProcessFinished() || currentProccess.isEmpty()) {
			Collections.shuffle(processes);
			currentProccess = processes.stream().filter(process -> !process.isFinished()).findFirst();
		}
	}

	private boolean isQuantFinished(){
		return tick % Constants.QUANT_PERIOD == 0;
	}

	private boolean isCurrentProcessFinished(){
		return currentProccess.isPresent() && currentProccess.get().isFinished();
	}

	private void generateNewProcess(){
		if (ThreadLocalRandom.current().nextDouble() < Constants.NEW_PROCESS_GENERATION_PROBABILITY ){
			processes.add(new Process(proccessCreated + 1,
					ThreadLocalRandom.current().nextInt(Constants.PROCESS_MIN_WORK_TIME, Constants.PROCESS_MAX_WORK_TIME + 1))
			);
			proccessCreated += 1;
		}
	}

	private boolean isFinishExecution(){
		return tick % Constants.PROGRAM_DURATION ==0;
	}

	private List<Process> createProcesses(){
		List<Process> processes = new ArrayList<>();
		for (int i = 0; i < Constants.START_PROCESS_NUMBER; i++){
			processes.add(new Process(i,
					ThreadLocalRandom.current().nextInt(Constants.PROCESS_MIN_WORK_TIME, Constants.PROCESS_MAX_WORK_TIME + 1))
			);
		}
		return processes;
	}

	public static List<PhysicalPage> getPhysicalPages()
	{
		return physicalPages;
	}
}
