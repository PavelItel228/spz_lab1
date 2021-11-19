import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


public class MMU
{
	private List<VirtualPage> workingSet;
	private List<VirtualPage> nonWorkingSet;
	private final Integer pid;

	public MMU(final Integer pid)
	{
		this.pid = pid;
		workingSet = new ArrayList<>();
		nonWorkingSet = new ArrayList<>();
		createVirtualPagesSets();
	}

	public void performMemoryCheck(){
		Stream.concat(workingSet.stream(), nonWorkingSet.stream()).filter(VirtualPage::isR).forEach(virtualPage -> {
			virtualPage.getPhysicalPage().ifPresent(physicalPage -> {
				physicalPage.setLastUseTick(Controller.tick);
			});
			virtualPage.setR(false);
		});
		regeneratePagesSets();
	}

	public void modifyPage(){
		System.out.println(String.format(Constants.PAGE_MODIFICATION_TRY, pid, Controller.tick));
		VirtualPage virtualPage = getAccessedVirtualPage();
		if (!virtualPage.isP()){
			System.out.println(String.format(Constants.PAGE_FAULT, pid, Controller.tick));
			loadPageToMemory(virtualPage);
		}
		virtualPage.setR(true);
		virtualPage.setM(true);
		System.out.println(String.format(Constants.PAGE_MODIFICATION, pid, Controller.tick));
	}

	public void readPage(){
		System.out.println(String.format(Constants.PAGE_READ_TRY, pid, Controller.tick));
		VirtualPage virtualPage = getAccessedVirtualPage();
		if (!virtualPage.isP()){
			System.out.println(String.format(Constants.PAGE_FAULT, pid, Controller.tick));
			loadPageToMemory(virtualPage);
		}
		virtualPage.setR(true);
		System.out.println(String.format(Constants.PAGE_READ, pid, Controller.tick));
	}

	private void loadPageToMemory(VirtualPage virtualPage) {
		PhysicalPage freePhysicalPage = getFreePhysicalPage();

		freePhysicalPage.setP(true);
		virtualPage.setPhysicalPage(Optional.ofNullable(freePhysicalPage));
		freePhysicalPage.setVirtualPage(Optional.of(virtualPage));
		virtualPage.setP(true);
		virtualPage.setR(false);
		virtualPage.setM(false);
	}

	private PhysicalPage getFreePhysicalPage(){
		if (Controller.getPhysicalPages().stream().anyMatch(PhysicalPage::isFree)){
			PhysicalPage page = Controller.getPhysicalPages().stream().filter(PhysicalPage::isFree).findFirst().get();
			page.setFree(false);
			return page;
		}

		Optional<VirtualPage> page= Stream.concat(workingSet.stream(), nonWorkingSet.stream()).filter(virtualPage -> virtualPage.isP()
				&& !virtualPage.isR()
				&&  Controller.tick - virtualPage.getPhysicalPage().get().getLastUseTick() > Constants.WORKING_SET_TTL)
				.findAny();
		if (page.isPresent()){
			return evictPage(page.get());
		}

		page = Controller.getPhysicalPages().stream()
				.sorted(Comparator.comparingInt(o -> o.getLastUseTick()))
				.filter(physicalPage -> physicalPage.getVirtualPage().isPresent())
				.map(physicalPage -> physicalPage.getVirtualPage().get()).findFirst();

		return evictPage(page.get());
	}

	public VirtualPage getAccessedVirtualPage(){
		if(isWorkingSetAccess()){
			return workingSet.get(ThreadLocalRandom.current().nextInt(0, workingSet.size()));
		} else {
			return nonWorkingSet.get(ThreadLocalRandom.current().nextInt(0, nonWorkingSet.size()));
		}
	}

	private PhysicalPage evictPage(VirtualPage page){
		if (page.isM()){
			System.out.println(String.format(Constants.DIRT_WRITE, pid, Controller.tick));
		}

		PhysicalPage physicalPage = page.getPhysicalPage().get();
		physicalPage.setP(false);
		physicalPage.setFree(true);
		page.setPhysicalPage(Optional.empty());
		physicalPage.setVirtualPage(Optional.empty());
		page.setP(false);
		page.setM(false);
		page.setR(false);

		return physicalPage;
	}

	private void regeneratePagesSets(){
		List<VirtualPage> pages = new ArrayList<>();
		pages.addAll(workingSet);
		pages.addAll(nonWorkingSet);
		Collections.shuffle(pages);
		workingSet = pages.subList(0, Constants.WORKING_SET_SIZE);
		nonWorkingSet = pages.subList(Constants.WORKING_SET_SIZE , pages.size());
	}

	private void createVirtualPagesSets(){
		for (int i = 0; i < Constants.VIRTUAL_MEMORY_SIZE - Constants.WORKING_SET_SIZE; i++)
		{
			nonWorkingSet.add(new VirtualPage());
		}
		for (int i = 0; i < Constants.WORKING_SET_SIZE; i++)
		{
			workingSet.add(new VirtualPage());
		}
	}

	private boolean isWorkingSetAccess() {
		return ThreadLocalRandom.current().nextDouble() < Constants.WORKING_SET_ACCESS_PROBABILITY;
	}

	public void freeMemory()
	{
		Stream.concat(workingSet.stream(), nonWorkingSet.stream()).forEach(virtualPage -> {
			virtualPage.getPhysicalPage().ifPresent(physicalPage -> {
				physicalPage.setFree(true);
				physicalPage.setVirtualPage(Optional.empty());
				physicalPage.setP(false);
			});
			virtualPage.setPhysicalPage(Optional.empty());
		});
	}
}
