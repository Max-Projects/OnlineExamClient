package teacher;

/**
 * Obsolete now
 * @author yao
 */
public class GarbageCollection {
    private Runtime runtime = Runtime.getRuntime();
    private final int mb = 1024*1024;
    
    private void runGC() throws Exception {
        long m1 = runtime.totalMemory()-runtime.freeMemory();
        long m2 = Long.MAX_VALUE;
        for (int i = 0; (m1 < m2)&&(i<runtime.maxMemory()); i++) {
            runtime.runFinalization();
            runtime.gc();
            Thread.currentThread().yield();
            m2 = m1;
            m1 = runtime.totalMemory()-runtime.freeMemory();
        }  
    }
    
    public String getMemoryDetail() {
        String str;
        str = "###### Memory - after gc ######\nMax: " + runtime.maxMemory()/mb 
                + "mb\nFree: " + runtime.freeMemory()/mb 
                + "mb\nUsed: " + (runtime.totalMemory()-runtime.freeMemory())/mb 
                + "mb\n####################";
        return str;
    }

}
