package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.campaign.SpecialItemSpecAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI.ShipTypeHints;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused") // dynamically loaded
public class ZsarHarsherEquipmentRestrictionsModPlugin extends BaseModPlugin {
	@Override
	public void onApplicationLoad() {
		this.addIndividualBlueprintsToItems();
	}

	private void addIndividualBlueprintsToItems() {
		final SettingsAPI settings = Global.getSettings();
		final List<String> blueprintTags = this.getAccessibleBlueprintPackageTags(settings);
		blueprintTags.add(Tags.WEAPON_REMNANTS);
		blueprintTags.add("derelict");

		final List<FighterWingSpecAPI> wings = settings.getAllFighterWingSpecs();
		for (final FighterWingSpecAPI wing : wings) {
			if (this.isBlueprintDroppableAndGroupedAndNotRare(wing, blueprintTags)) {
				wing.addTag(Items.TAG_RARE_BP);
			}
		}
		final List<ShipHullSpecAPI> ships = settings.getAllShipHullSpecs();
		for (final ShipHullSpecAPI ship : ships) {
			if (this.isBlueprintDroppableAndGroupedAndNotRare(ship, blueprintTags)) {
				ship.addTag(Items.TAG_RARE_BP);
			}
		}
		final List<WeaponSpecAPI> weapons = settings.getAllWeaponSpecs();
		for (final WeaponSpecAPI weapon : weapons) {
			if (this.isBlueprintDroppableAndGroupedAndNotRare(weapon, blueprintTags)) {
				weapon.addTag(Items.TAG_RARE_BP);
			}
		}
	}

	private List<String> getAccessibleBlueprintPackageTags(final SettingsAPI settings) {
		// I miss Java 17... -Zsar 2023-02-06
		/* return settings.getAllSpecialItemSpecs().stream()
		 *   .filter(item -> item.hasTag("package_bp"))
		 *   .filter(item -> !item.hasTag(Tags.NO_DROP))
		 *   .map(SpecialItemSpecAPI::getParams)
		 *   .collect(Collectors.toList());
		 */
		final ArrayList<String> blueprintPackages = new ArrayList<String>();
		final List<SpecialItemSpecAPI> specialItems = settings.getAllSpecialItemSpecs();
		for (final SpecialItemSpecAPI item : specialItems) {
			if (item.hasTag("package_bp") && !item.hasTag(Tags.NO_DROP)) {
				blueprintPackages.add(item.getParams());
			}
		}
		return blueprintPackages;
	}

	private boolean isBlueprintDroppableAndGroupedAndNotRare(final FighterWingSpecAPI wing, final List<String> blueprintTags) {
		// I miss Java 17... -Zsar 2023-02-06
		/* && blueprintTags.stream().anyMatch(tag -> wing.hasTag(tag)) */
		boolean hasAnyBlueprintTag = false;
		for (final String tag : blueprintTags) {
			hasAnyBlueprintTag = wing.hasTag(tag);
			if (hasAnyBlueprintTag)
				break;
		}
		return !wing.hasTag(Tags.NO_DROP)         // e.g. Terminator Drone
		    && !wing.hasTag(Items.TAG_RARE_BP)    // NO_OP
		    && hasAnyBlueprintTag
		    ;
	}

	private boolean isBlueprintDroppableAndGroupedAndNotRare(final ShipHullSpecAPI ship, final List<String> blueprintTags) {
		// I miss Java 17... -Zsar 2023-02-06
		/* && blueprintTags.stream().anyMatch(tag -> wing.hasTag(tag)) */
		boolean hasAnyBlueprintTag = false;
		for (final String tag : blueprintTags) {
			hasAnyBlueprintTag = ship.hasTag(tag);
			if (hasAnyBlueprintTag)
				break;
		}
		return !ship.getHints().contains(ShipTypeHints.STATION) // e.g. Mothership
		    && !ship.hasTag(Items.TAG_NO_DEALER)                // e.g. Merlon
		    && !ship.hasTag(Items.TAG_RARE_BP)                  // NO_OP
		    && hasAnyBlueprintTag;
	}

	private boolean isBlueprintDroppableAndGroupedAndNotRare(final WeaponSpecAPI weapon, final List<String> blueprintTags) {
		// I miss Java 17... -Zsar 2023-02-06
		/* && blueprintTags.stream().anyMatch(tag -> wing.hasTag(tag)) */
		boolean hasAnyBlueprintTag = false;
		for (final String tag : blueprintTags) {
			hasAnyBlueprintTag = weapon.hasTag(tag);
			if (hasAnyBlueprintTag)
				break;
		}
		return !weapon.hasTag(Items.TAG_RARE_BP) // NO_OP
		    && hasAnyBlueprintTag;
	}
}
