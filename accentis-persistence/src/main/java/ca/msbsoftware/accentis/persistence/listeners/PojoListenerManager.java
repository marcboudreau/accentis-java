package ca.msbsoftware.accentis.persistence.listeners;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import ca.msbsoftware.accentis.persistence.pojos.BaseObject;

public class PojoListenerManager {

	private static PojoListenerManager instance = new PojoListenerManager();
	
	private Set<IPojoListener> pojoListeners = new HashSet<IPojoListener>();

	abstract class PojoRunnable implements Runnable {

		protected IPojoListener listener;
		
		protected BaseObject object;
		
		public void setListenerAndObject(IPojoListener listener, BaseObject object) {
			this.listener = listener;
			this.object = object;
		}
	}
	
	private PojoRunnable pojoCreatedRunnable = new PojoRunnable() {
		@Override
		public void run() {
			listener.pojoCreated(object);
		}
	};
	
	private PojoRunnable pojoDeletedRunnable = new PojoRunnable() {
		@Override
		public void run() {
			listener.pojoDeleted(object);
		}
	};
	
	private PojoRunnable pojoRefreshedRunnable = new PojoRunnable() {
		@Override
		public void run() {
			listener.pojoRefreshed(object);
		}
	};
	
	private PojoRunnable pojoSavedRunnable = new PojoRunnable() {
		@Override
		public void run() {
			listener.pojoSaved(object);
		}
	};
	
	public static PojoListenerManager getInstance() {
		return instance;
	}
	
	public void addPojoListener(IPojoListener listener) {
		synchronized (pojoListeners) {
			pojoListeners.add(listener);
		}
	}
	
	public void removePojoListener(IPojoListener listener) {
		synchronized (pojoListeners) {
			pojoListeners.remove(listener);
		}
	}
	
	public void objectCreated(BaseObject object) {
		synchronized (pojoListeners) {
			for (IPojoListener listener : pojoListeners)
				if (listener.listensToClass(object.getClass()))
					invokeRunnable(getPojoCreatedRunnable(listener, object));
		}
	}
	
	public void objectDeleted(BaseObject object) {
		synchronized (pojoListeners) {
			for (IPojoListener listener : pojoListeners)
				if (listener.listensToClass(object.getClass()))
					invokeRunnable(getPojoDeletedRunnable(listener, object));
		}
	}
	
	public void objectRefreshed(BaseObject object) {
		synchronized (pojoListeners) {
			for (IPojoListener listener : pojoListeners)
				if (listener.listensToClass(object.getClass()))
					invokeRunnable(getPojoRefreshedRunnable(listener, object));
		}
	}
	
	public void objectSaved(BaseObject object) {
		synchronized (pojoListeners) {
			for (IPojoListener listener : pojoListeners)
				if (listener.listensToClass(object.getClass()))
					invokeRunnable(getPojoSavedRunnable(listener, object));
		}
	}
	
	private void invokeRunnable(Runnable runnable) {
		if (EventQueue.isDispatchThread())
			runnable.run();
		else
			try {
				EventQueue.invokeAndWait(runnable);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	protected Runnable getPojoCreatedRunnable(IPojoListener listener, BaseObject object) {
		pojoCreatedRunnable.setListenerAndObject(listener, object);
		return pojoCreatedRunnable;
	}
	
	protected Runnable getPojoDeletedRunnable(IPojoListener listener, BaseObject object) {
		pojoDeletedRunnable.setListenerAndObject(listener, object);
		return pojoDeletedRunnable;
	}
	
	protected Runnable getPojoRefreshedRunnable(IPojoListener listener, BaseObject object) {
		pojoRefreshedRunnable.setListenerAndObject(listener, object);
		return pojoRefreshedRunnable;
	}
	
	protected Runnable getPojoSavedRunnable(IPojoListener listener, BaseObject object) {
		pojoSavedRunnable.setListenerAndObject(listener, object);
		return pojoSavedRunnable;
	}
}
