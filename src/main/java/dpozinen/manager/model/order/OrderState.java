package dpozinen.manager.model.order;

/**
 * @author dpozinen
 */
public enum OrderState {
	DELAYED, DONE, QUEUED,
	IN_PROGRESS {
		@Override
		public String toString() {
			return "IN PROGRESS";
		}
	},

	PAYED,
	NOT_PAYED {
		@Override
		public String toString() {
			return "NOT PAYED";
		}
	}
}
