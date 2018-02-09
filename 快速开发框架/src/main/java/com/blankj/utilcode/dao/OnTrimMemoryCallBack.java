package com.blankj.utilcode.dao;


/**
 * Created by leilei on 2017/7/29.
 */

public interface OnTrimMemoryCallBack {
    /**
     * 回调只有当我们程序中的所有UI组件全部不可见的时候才会触发，
     * 这和onStop()方法还是有很大区别的，
     * 因为onStop()方法只是当一个Activity完全不可见的时候就会调用，
     * 比如说用户打开了我们程序中的另一个Activity。因此，
     * 我们可以在onStop()方法中去释放一些Activity相关的资源，
     * 比如说取消网络连接或者注销广播接收器等，
     * 但是像UI相关的资源应该一直要等到onTrimMemory(TRIM_MEMORY_UI_HIDDEN)这个回调之后才去释放，
     * 这样可以保证如果用户只是从我们程序的一个Activity回到了另外一个Activity，
     * 界面相关的资源都不需要重新加载，
     * 从而提升响应速度。
     * */
    void TRIM_MEMORY_UI_HIDDEN();

    /**
     * 表示应用程序正常运行，
     * 并且不会被杀掉。
     * 但是目前手机的内存已经有点低了，
     * 系统可能会开始根据LRU缓存规则来去杀死进程了。
     */
    void TRIM_MEMORY_RUNNING_MODERATE();

    /**
     * 表示应用程序正常运行，
     * 并且不会被杀掉。
     * 但是目前手机的内存已经非常低了，
     * 我们应该去释放掉一些不必要的资源以提升系统的性能，
     * 同时这也会直接影响到我们应用程序的性能。
     */
    void TRIM_MEMORY_RUNNING_LOW();

    /**
     * 表示应用程序仍然正常运行，
     * 但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。
     * 这个时候我们应当尽可能地去释放任何不必要的资源，
     * 不然的话系统可能会继续杀掉所有缓存中的进程，
     * 并且开始杀掉一些本来应当保持运行的进程，
     * 比如说后台运行的服务。
     */
    void TRIM_MEMORY_RUNNING_CRITICAL();

    /**
     * 表示手机目前内存已经很低了，
     * 系统准备开始根据LRU缓存来清理进程。
     * 这个时候我们的程序在LRU缓存列表的最近位置，
     * 是不太可能被清理掉的，
     * 但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，
     * 从而让我们的程序更长时间地保留在缓存当中，
     * 这样当用户返回我们的程序时会感觉非常顺畅，
     * 而不是经历了一次重新启动的过程。
     */
    void TRIM_MEMORY_BACKGROUND();

    /**
     * 表示手机目前内存已经很低了，
     * 并且我们的程序处于LRU缓存列表的中间位置，
     * 如果手机内存还得不到进一步释放的话，
     * 那么我们的程序就有被系统杀掉的风险了。
     */
    void TRIM_MEMORY_MODERATE();

    /**
     * 表示手机目前内存已经很低了，
     * 并且我们的程序处于LRU缓存列表的最边缘位置，
     * 系统会最优先考虑杀掉我们的应用程序，
     * 在这个时候应当尽可能地把一切可以释放的东西都进行释放。
     */
    void TRIM_MEMORY_COMPLETE();

}
