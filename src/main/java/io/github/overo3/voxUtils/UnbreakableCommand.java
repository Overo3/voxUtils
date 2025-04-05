package io.github.overo3.voxUtils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("UnstableApiUsage")
public class UnbreakableCommand {
    LiteralArgumentBuilder<CommandSourceStack> commandStack = Commands.literal("unbreakable")
            // Base command without arguments (let me make things optional I pray)
            .executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender();
                Entity executor = ctx.getSource().getExecutor();

                if (executor instanceof Player player) {
                    ItemStack item = player.getInventory().getItemInMainHand();

                    // Why is hasItemMeta not true on non-air items...
                    if (item.getType().isAir() || !item.hasItemMeta()) {
                        sender.sendMessage("You're not holding anything, or the item has no metadata.");
                        return Command.SINGLE_SUCCESS;
                    }

                    // Make thingy unbreakable (works on things that don't even have durability)
                    ItemMeta meta = item.getItemMeta();
                    if (meta.isUnbreakable()) {
                        meta.setUnbreakable(false);
                        sender.sendMessage("Item is no longer unbreakable.");
                    } else {
                        meta.setUnbreakable(true);
                        sender.sendMessage("Item is now unbreakable.");
                    }

                    // Sync meta (might be wasteful call? look into mirrors later)
                    item.setItemMeta(meta);
                } else {
                    sender.sendMessage("Non-living entities cannot have this command applied to them.");
                }

                return Command.SINGLE_SUCCESS;
            })
            //Make "hidden" overload for opt argument (why isn't this a builtin)
            .then(Commands.argument("hidden", BoolArgumentType.bool())
                    .executes(ctx -> {
                        CommandSender sender = ctx.getSource().getSender();
                        Entity executor = ctx.getSource().getExecutor();

                        if (executor instanceof Player player) {
                            ItemStack item = player.getInventory().getItemInMainHand();

                            // Why is hasItemMeta not true on non-air items...
                            if (item.getType().isAir() || !item.hasItemMeta()) {
                                sender.sendMessage("You're not holding anything, or the item has no metadata.");
                                return Command.SINGLE_SUCCESS;
                            }

                            // Make thingy unbreakable (works on things that don't even have durability)
                            ItemMeta meta = item.getItemMeta();
                            if (meta.isUnbreakable()) {
                                meta.setUnbreakable(false);
                                sender.sendMessage("Item is no longer unbreakable.");
                            } else {
                                meta.setUnbreakable(true);
                                sender.sendMessage("Item is now unbreakable.");
                            }

                            // Make property hidden (just let binary cook)
                            if (ctx.getArgument("hidden", Boolean.class)) {
                                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                sender.sendMessage("The unbreakable property is now hidden.");
                            } else {
                                meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                sender.sendMessage("The unbreakable property is no longer hidden.");
                            }

                            // Sync meta (might be wasteful call? look into mirrors later)
                            item.setItemMeta(meta);
                        } else {
                            sender.sendMessage("Non-living entities cannot have this command applied to them.");
                        }

                        return Command.SINGLE_SUCCESS;
                    }));
}

