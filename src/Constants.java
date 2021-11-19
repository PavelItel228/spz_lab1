public class Constants
{
	public static final Integer MEMORY_CHECK_TIME = 30;
	public static final Integer QUANT_PERIOD = 5;
	public static final Double NEW_PROCESS_GENERATION_PROBABILITY = 0.09;
	public static final Integer START_PROCESS_NUMBER = 5;
	public static final Integer PROCESS_MIN_WORK_TIME = 9;
	public static final Integer PROCESS_MAX_WORK_TIME = 11;
	public static final Integer PROGRAM_DURATION = 200;
	public static final Integer WORKING_SET_SIZE = 4;
	public static final Integer VIRTUAL_MEMORY_SIZE = 6;
	public static final Integer PHYSICAL_MEMORY_SIZE = 4;
	public static final Integer WORKING_SET_TTL = 25;
	public static final String NO_WORKING_PROCESSES = "no working processes | tick %d";
	public static final String PAGE_FAULT = "page fault | process %d | tick %d";
	public static final String PAGE_MODIFICATION_TRY = "page modification try | process %d | tick %d";
	public static final String PAGE_READ_TRY = "page read try | process %d | tick %d";
	public static final String PAGE_MODIFICATION = "page modification success | process %d | tick %d";
	public static final String PAGE_READ = "page read success | process %d | tick %d";
	public static final String DIRT_WRITE = "writing dirty page to disk | process %d | tick %d";
	public static final Double PAGE_ACCESS_PROBABILITY = 0.5;
	public static final Double PAGE_MODIFICATION_PROBABILITY = 0.5;
	public static final Double WORKING_SET_ACCESS_PROBABILITY = 0.9;
}
