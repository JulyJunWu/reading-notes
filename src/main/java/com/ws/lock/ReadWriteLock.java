package com.ws.lock;

/**
 * @author :Jun
 * date : 2019-08-03 23:41
 */
public interface ReadWriteLock {

    Lock readLock();

    Lock writeLock();

    /**
     * 当前多少线程写操作线程数量
     * @return
     */
    int getWritingWriters();

    /**
     * 等待写锁的线程数量
     * @return
     */
    int getWaitingWriters();

    /**
     * 获取读锁的线程数量
     * @return
     */
    int getReadingReaders();

    static ReadWriteLock readWriteLock() {
        return new ReadWriterLockImpl();
    }

    static ReadWriteLock readWriteLock(boolean preferWriter) {
        return new ReadWriterLockImpl(preferWriter);
    }

    class ReadWriterLockImpl implements ReadWriteLock {

        private final Object MUTEX = new Object();

        private volatile int writingWriters = 0;

        private volatile int readingReaders = 0;

        private volatile int waitingWriters = 0;


        public ReadWriterLockImpl(boolean preferWriter) {
        }

        public ReadWriterLockImpl() {
            this(true);
        }


        @Override
        public Lock readLock() {
            return new ReadLock(this);
        }

        @Override
        public Lock writeLock() {
            return new WriteLock(this);
        }

        @Override
        public int getWritingWriters() {
            return writingWriters;
        }


        public void decrementWritingWriters() {
            this.writingWriters--;
        }

        public void incrementWritingWriters() {
            this.writingWriters++;
        }


        @Override
        public int getWaitingWriters() {
            return waitingWriters;
        }

        public void decrementWaitingWriters() {
            this.waitingWriters--;
        }

        public void incrementWaitingWriters() {
            this.waitingWriters++;
        }


        @Override
        public int getReadingReaders() {
            return readingReaders;
        }

        public void decrementReadingReaders() {
            this.readingReaders--;
        }

        public void incrementReadingReaders() {
            this.readingReaders++;
        }

        public Object getMUTEX() {
            return MUTEX;
        }

    }

    /**
     * 读锁
     */
    class ReadLock implements Lock {

        private ReadWriterLockImpl readWriterLock;

        public ReadLock(ReadWriterLockImpl readWriterLock) {
            this.readWriterLock = readWriterLock;
        }

        @Override
        public void lock() throws InterruptedException {

            synchronized (readWriterLock.getMUTEX()) {

                while (readWriterLock.getWritingWriters() > 0) {
                    readWriterLock.getMUTEX().wait();
                }
                //获取锁成功,将读锁数加一
                readWriterLock.incrementReadingReaders();
            }

        }

        @Override
        public void unLock() {

            synchronized (readWriterLock.getMUTEX()) {

                readWriterLock.decrementReadingReaders();
                readWriterLock.getMUTEX().notifyAll();
            }

        }
    }

    /**
     * 写锁
     */
    class WriteLock implements Lock {

        private ReadWriterLockImpl readWriterLockImpl;

        public WriteLock(ReadWriterLockImpl readWriterLockImpl) {
            this.readWriterLockImpl = readWriterLockImpl;
        }


        @Override
        public void lock() throws InterruptedException {

            synchronized (readWriterLockImpl.getMUTEX()) {

                //将写入等待锁++;
                readWriterLockImpl.incrementWaitingWriters();

                while (readWriterLockImpl.getWritingWriters() > 0 || readWriterLockImpl.getReadingReaders() > 0) {
                    readWriterLockImpl.getMUTEX().wait();
                }

                //获取成功锁,写入等待锁--;
                readWriterLockImpl.decrementWaitingWriters();

                //将正在写入的锁++
                readWriterLockImpl.incrementWritingWriters();
            }

        }

        @Override
        public void unLock() {

            synchronized (readWriterLockImpl.getMUTEX()) {
                //写入锁--
                readWriterLockImpl.decrementWritingWriters();

                //唤醒所有wait set 中的线程
                readWriterLockImpl.getMUTEX().notifyAll();
            }
        }
    }

}
