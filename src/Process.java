import java.util.concurrent.ThreadLocalRandom;


public class Process
{
	private MMU mmu;
	private boolean isFinished;
	private Integer pid;
	private Integer timeToWorkLeft;

	public Process(Integer pid, Integer timeToWork)
	{
		this.mmu = new MMU(pid);
		this.isFinished = false;
		this.pid = pid;
		this.timeToWorkLeft = timeToWork;
	}

	public void run() {
		accessPage();
		timeToWorkLeft-= 1;
		if (timeToWorkLeft == 0){
			mmu.freeMemory();
			setFinished(true);
		}
	}

	private void accessPage() {
		if (isPageAccessed()){
			if (isPageModification()){
				mmu.modifyPage();
			} else {
				mmu.readPage();
			}
		}
	}

	private boolean isPageAccessed() {
		return ThreadLocalRandom.current().nextDouble() < Constants.PAGE_ACCESS_PROBABILITY;
	}

	private boolean isPageModification(){
		return ThreadLocalRandom.current().nextDouble() < Constants.PAGE_MODIFICATION_PROBABILITY;
	}

	public MMU getMmu()
	{
		return mmu;
	}

	public void setMmu(MMU mmu)
	{
		this.mmu = mmu;
	}

	public boolean isFinished()
	{
		return isFinished;
	}

	public void setFinished(boolean finished)
	{
		isFinished = finished;
	}
}
