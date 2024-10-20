package com.walhalla.telegramstickers;

import android.content.Context;

import com.telegramstickers.catalogue.R;
import com.walhalla.stickers.constants.Constants;

public class ResourceHelper {
    public final int[] data;
    private static ResourceHelper instance;

    public static ResourceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ResourceHelper(context);
        }
        return instance;
    }

    public ResourceHelper(Context context) {

        this.data = new int[]{
                R.string.c_aesthetics,
                R.string.c_aizen,
                R.string.c_alcohol,
                R.string.c_alphabet,
                R.string.c_animals,
                R.string.c_anime,
                R.string.c_arknights,
                R.string.c_army,
                R.string.c_baki,
                R.string.c_bat,
                R.string.c_bear,
                R.string.c_beaver,
                R.string.c_bees,
                R.string.c_berserk,
                R.string.c_bitcoin,
                R.string.c_bmw,
                R.string.c_boar,
                R.string.c_bratishkin,
                R.string.c_bsd,
                R.string.c_capybara,
                R.string.c_cars,
                R.string.c_cats,
                R.string.c_chicken,
                R.string.c_children,
                R.string.c_chinese,
                R.string.c_cock,
                R.string.c_corgi,
                R.string.c_cow,
                R.string.c_cursed,
                R.string.c_cyberpunk,
                R.string.c_dachshund,
                R.string.c_dance,
                R.string.c_dazai,
                R.string.c_deer,
                R.string.c_deltarune,
                R.string.c_depression,
                R.string.c_deutsch,
                R.string.c_dinosaurs,
                R.string.c_dogs,
                R.string.c_dragons,
                R.string.c_ducks,
                R.string.c_eagle,
                R.string.c_edgar,
                R.string.c_edgy,
                R.string.c_elf,
                R.string.c_eminem,
                R.string.c_emojis,
                R.string.c_english,
                R.string.c_enhypen,
                R.string.c_euphoria,
                R.string.c_evangelion,
                R.string.c_extreme,
                R.string.c_eye,
                R.string.c_fallout,
                R.string.c_feet,
                R.string.c_felix,
                R.string.c_fire,
                R.string.c_flork,
                R.string.c_fluttershy,
                R.string.c_font,
                R.string.c_food,
                R.string.c_fortnite,
                R.string.c_french,
                R.string.c_frog,
                R.string.c_frogs,
                R.string.c_furry,
                R.string.c_futurama,
                R.string.c_gangster,
                R.string.c_garfield,
                R.string.c_genshin,
                R.string.c_gifs,
                R.string.c_gigachad,
                R.string.c_girls,
                R.string.c_gojo,
                R.string.c_gore,
                R.string.c_griffith,
                R.string.c_gta,
                R.string.c_guys,
                R.string.c_halloween,
                R.string.c_hamsters,
                R.string.c_hanna,
                R.string.c_hasbulla,
                R.string.c_hedgehog,
                R.string.c_hentai,
                R.string.c_homelander,
                R.string.c_horimiya,
                R.string.c_horny,
                R.string.c_hugs,
                R.string.c_hyunjin,
                R.string.c_icq,
                R.string.c_india,
                R.string.c_instasamka,
                R.string.c_italian,
                R.string.c_itzy,
                R.string.c_ive,
                R.string.c_japan,
                R.string.c_japanese,
                R.string.c_jdm,
                R.string.c_jesus,
                R.string.c_jew,
                R.string.c_jinx,
                R.string.c_kakaotalk,
                R.string.c_kanye,
                R.string.c_kardashian,
                R.string.c_katz,
                R.string.c_kawaii,
                R.string.c_kazakhs,
                R.string.c_kenma,
                R.string.c_kirby,
                R.string.c_kish,
                R.string.c_kizaru,
                R.string.c_knights,
                R.string.c_kokomi,
                R.string.c_lali,
                R.string.c_lesbi,
                R.string.c_linux,
                R.string.c_lion,
                R.string.c_lizard,
                R.string.c_lololoshka,
                R.string.c_lotr,
                R.string.c_love,
                R.string.c_lucifer,
                R.string.c_luffy,
                R.string.c_lust,
                R.string.c_mafia,
                R.string.c_makima,
                R.string.c_manhwa,
                R.string.c_marvel,
                R.string.c_member,
                R.string.c_memes,
                R.string.c_minecraft,
                R.string.c_minho,
                R.string.c_minions,
                R.string.c_monkey,
                R.string.c_monkeys,
                R.string.c_mushroom,
                R.string.c_nana,
                R.string.c_nanami,
                R.string.c_napoleon,
                R.string.c_napoli,
                R.string.c_narcos,
                R.string.c_naruto,
                R.string.c_nasty,
                R.string.c_naughty,
                R.string.c_nba,
                R.string.c_nft,
                R.string.c_nijisanji,
                R.string.c_nike,
                R.string.c_nsfw,
                R.string.c_obanai,
                R.string.c_obladaet,
                R.string.c_octopus,
                R.string.c_opm,
                R.string.c_opossum,
                R.string.c_otters,
                R.string.c_overlord,
                R.string.c_overwatch,
                R.string.c_owls,
                R.string.c_penguins,
                R.string.c_pharaoh,
                R.string.c_phrases,
                R.string.c_pigs,
                R.string.c_pokemon,
                R.string.c_poker,
                R.string.c_police,
                R.string.c_portal,
                R.string.c_premium,
                R.string.c_programmer,
                R.string.c_pubg,
                R.string.c_pumpkin,
                R.string.c_qoobee,
                R.string.c_quby,
                R.string.c_raccoon,
                R.string.c_ranfren,
                R.string.c_rapper,
                R.string.c_rapunzel,
                R.string.c_rat,
                R.string.c_rats,
                R.string.c_rengoku,
                R.string.c_robots,
                R.string.c_rome,
                R.string.c_rude,
                R.string.c_sanrio,
                R.string.c_sekai,
                R.string.c_sex,
                R.string.c_sexy,
                R.string.c_shrek,
                R.string.c_sigma,
                R.string.c_smoking,
                R.string.c_soprano,
                R.string.c_space,
                R.string.c_spiders,
                R.string.c_stitch,
                R.string.c_sukuna,
                R.string.c_supernatural,
                R.string.c_tanks,
                R.string.c_teacher,
                R.string.c_terraria,
                R.string.c_tsoi,
                R.string.c_turtle,
                R.string.c_twilight,
                R.string.c_uber,
                R.string.c_ufc,
                R.string.c_ugly,
                R.string.c_umaru,
                R.string.c_umbrella,
                R.string.c_undertale,
                R.string.c_unicorn,
                R.string.c_unicorns,
                R.string.c_univer,
                R.string.c_usa,
                R.string.c_username,
                R.string.c_ussr,
                R.string.c_uwu,
                R.string.c_vagabond,
                R.string.c_valakas,
                R.string.c_valorant,
                R.string.c_vampire,
                R.string.c_vampires,
                R.string.c_vanitas,
                R.string.c_vietnam,
                R.string.c_vintage,
                R.string.c_vocaloid,
                R.string.c_vodka,
                R.string.c_volleyball,
                R.string.c_vore,
                R.string.c_vtuber,
                R.string.c_warcraft,
                R.string.c_wechat,
                R.string.c_weeknd,
                R.string.c_whale,
                R.string.c_wine,
                R.string.c_witch,
                R.string.c_wolves,
                R.string.c_work,
                R.string.c_writers,
                R.string.c_xbox,
                R.string.c_xiao,
                R.string.c_xxxtentacion,
                R.string.c_yakuza,
                R.string.c_yandere,
                R.string.c_yato,
                R.string.c_yoga,
                R.string.c_yoll,
                R.string.c_yoshi,
                R.string.c_yugioh,
                R.string.c_yuna,
                R.string.c_yunjin,
                R.string.c_yuta,
                R.string.c_zara,
                R.string.c_zelda,
                R.string.c_zhdun,
                R.string.c_zhongli,
                R.string.c_zombie,
                R.string.c_zootopia,
                R.string.c_zoro,
                R.string.c_zxc,
                R.string.c_zxcursed
        };
    }

    public int[] categories() {
        return data;
    }


    public boolean isMenuPressed(int id) {
        boolean result = false;
        for (int i : data) {
            if (i == id) {
                result = true;
                break;
            }
        }
        //DLog.d("menu-pressed: " + id + " " + result);
        return result;
    }

    public int toType(int resourceId) {
        int position = Constants.D_ALL;
        for (int i = 0; i < data.length; i++) {
            int aa = data[i];
            if (aa == resourceId) {
                position = i;
                break;
            }
        }
        //return (position == 0) ? Constants.D_ALL : position;
        return position;
    }

    public int titleV(int c_id) {
        if (c_id == Constants.D_FAVORITE) {
            return R.string.title_favorite_statuses;
        } else if (c_id == Constants.D_ALL) {
            return R.string.dictionary_all;
        } else if (c_id < 0 || c_id > data.length - 1) {
            return c_id;
        }
        return data[c_id];
    }
}
