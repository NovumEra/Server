package com.rs2.server.model.entity.player;

import static com.rs2.server.model.entity.player.Abilities.Ability.*;

public class Abilities {

	private long abilities = 0;

	public Abilities() {
		addAbility(PLAYER);
		setPrimaryAbility(PLAYER);
	}

	public void setPrimaryAbility(Ability ability) {
		if(hasAbility(ability)) {
			abilities = abilities & ~((abilities >> shift) << shift);
			abilities = abilities | (ability.ordinal() << shift);
		}
	}

	public Ability getPrimaryAbility() {
		return forIndex(abilities >> shift);
	}

	public void addAbility(Ability ability) {
		if(!isAbilityToggled(ability))
			abilities |= ability.getMask();
	}

	public void removeAbility(Ability ability) {
		if(ability == PLAYER)
			return;
		if(getPrimaryAbility() == ability)
			setPrimaryAbility(PLAYER);
		abilities &= ~ability.getMask();
	}

	public boolean isAbilityToggled(Ability ability) {
		return ability == PLAYER || (abilities & ability.getMask()) == ability.getMask();
	}

	public boolean hasAbility(Ability... abilities) {
		if(isAbilityToggled(OWNER))
			return true;
		for(Ability ability : abilities) {
			if(ability == PLAYER)
				return true;
			if(isAbilityToggled(ability))
				return true;
			for(int i = ADMINISTRATOR.ordinal(); i >= ability.ordinal(); i--) {
				if(isAbilityToggled(forIndex(i)))
					return true;
			}
		}
		return false;
	}

	public static enum Ability {

		PLAYER, HELPER, MODERATOR, GLOBAL_MODERATOR, HEAD_MODERATOR, ADMINISTRATOR, OWNER;

		public long getMask() {
			return 1L << ordinal();
		}

		public static int shift = 0;

		static {
			shift = values().length + 8;
		}

		public static Ability forIndex(final long index) {
			if(index >= values().length)
				return PLAYER;
			return values()[((int) index)];
		}
	}
}