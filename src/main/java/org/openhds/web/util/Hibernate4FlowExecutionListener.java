package org.openhds.web.util;

//package org.springframework.webflow.persistence;

import org.hibernate.FlushMode;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
 
public class Hibernate4FlowExecutionListener extends FlowExecutionListenerAdapter {
     
    /**
     * The name of the attribute the flow {@link Session persistence context} is indexed under.
     */
    public static final String PERSISTENCE_CONTEXT_ATTRIBUTE = "persistenceContext";
 
    private SessionFactory sessionFactory;
 
    private TransactionTemplate transactionTemplate;
 
    private Interceptor entityInterceptor;
 
    /**
     * Create a new Hibernate Flow Execution Listener using giving Hibernate session factory and transaction manager.
     * @param sessionFactory the session factory to use
     * @param transactionManager the transaction manager to drive transactions
     */
    public Hibernate4FlowExecutionListener(SessionFactory sessionFactory, PlatformTransactionManager transactionManager) {
        this.sessionFactory = sessionFactory;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }
 
    /**
     * Sets the entity interceptor to attach to sessions opened by this listener.
     * @param entityInterceptor the entity interceptor
     */
    public void setEntityInterceptor(Interceptor entityInterceptor) {
        this.entityInterceptor = entityInterceptor;
    }
 
    public void sessionStarting(RequestContext context, FlowSession session, MutableAttributeMap input) {
        boolean reusePersistenceContext = false;
        if (isParentPersistenceContext(session)) {
            if (isPersistenceContext(session.getDefinition())) {
                setHibernateSession(session, getHibernateSession(session.getParent()));
                reusePersistenceContext = true;
            } else {
                unbind(getHibernateSession(session.getParent()));
            }
        }
        if (isPersistenceContext(session.getDefinition()) && (!reusePersistenceContext)) {
            Session hibernateSession = createSession(context);
            setHibernateSession(session, hibernateSession);
            bind(hibernateSession);
        }
    }
 
    public void paused(RequestContext context) {
        if (isPersistenceContext(context.getActiveFlow())) {
            Session session = getHibernateSession(context.getFlowExecutionContext().getActiveSession());
            unbind(session);
            session.disconnect();
        }
    }
 
    public void resuming(RequestContext context) {
        if (isPersistenceContext(context.getActiveFlow())) {
            bind(getHibernateSession(context.getFlowExecutionContext().getActiveSession()));
        }
    }
 
    public void sessionEnding(RequestContext context, FlowSession session, String outcome, MutableAttributeMap output) {
        if (isParentPersistenceContext(session)) {
            return;
        }
        if (isPersistenceContext(session.getDefinition())) {
            final Session hibernateSession = getHibernateSession(session);
            Boolean commitStatus = session.getState().getAttributes().getBoolean("commit");
            if (Boolean.TRUE.equals(commitStatus)) {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        sessionFactory.getCurrentSession();
                        // nothing to do; a flush will happen on commit automatically as this is a read-write
                        // transaction
                    }
                });
            }
            unbind(hibernateSession);
            hibernateSession.close();
        }
    }
 
    public void sessionEnded(RequestContext context, FlowSession session, String outcome, AttributeMap output) {
        if (isParentPersistenceContext(session)) {
            if (!isPersistenceContext(session.getDefinition())) {
                bind(getHibernateSession(session.getParent()));
            }
        }
    }
 
    public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
        if (context.getFlowExecutionContext().isActive()) {
            if (isPersistenceContext(context.getActiveFlow())) {
                unbind(getHibernateSession(context.getFlowExecutionContext().getActiveSession()));
            }
        }
    }
 
    // internal helpers
 
    private boolean isPersistenceContext(FlowDefinition flow) {
        return flow.getAttributes().contains(PERSISTENCE_CONTEXT_ATTRIBUTE);
    }
 
    private boolean isParentPersistenceContext(FlowSession flowSession) {
        return ((!flowSession.isRoot()) && isPersistenceContext(flowSession.getParent().getDefinition()));
    }
 
    private Session createSession(RequestContext context) {
        Session session = (entityInterceptor != null ? sessionFactory.withOptions().interceptor(entityInterceptor).openSession():
            sessionFactory.openSession());
        session.setFlushMode(FlushMode.MANUAL);
        return session;
    }
 
    private Session getHibernateSession(FlowSession session) {
        return (Session) session.getScope().get(PERSISTENCE_CONTEXT_ATTRIBUTE);
    }
 
    private void setHibernateSession(FlowSession session, Session hibernateSession) {
        session.getScope().put(PERSISTENCE_CONTEXT_ATTRIBUTE, hibernateSession);
    }
 
    private void bind(Session session) {
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }
 
    private void unbind(Session session) {
        if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
        }
    }
}