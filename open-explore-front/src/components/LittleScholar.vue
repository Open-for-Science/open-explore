<template>
<x-card :shadow="shadow" style="background-color: transparent;">
    <div class="LittleScholar__warp" :class="{'LittleScholar__warp--hover':(scholar.uid || scholar.id)}" @click="goProfileDetail">
        <avatar :url="scholar.avatar" :size="avatarSize" round></avatar>
        <div class="LittleScholar__info" :class="{'LittleScholar__info--noIns':!showIns}">
            <div class="LittleScholar__info--item LittleScholar__info--name">{{scholar.name}}
                <base-tooltip v-if="'1' ==  scholar.isTop " content="认证大V">
                    <svg style="margin-left: 5px;" t="1697534518323" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4306" width="16" height="16"><path d="M670.851209 210.071219Q338.592909-257.843783 289.734085 316.478946q-567.930947 173.664363-22.525599 383.106694-18.626498 575.22604 367.224432 130.351181 556.428027 182.034281 249.473866-302.460477 362.467757-462.712389-213.055575-317.439428z m51.545888 144.518296L513.217754 776.984512a22.136832 22.136832 0 0 1-41.08349-3.201608l-87.678321-278.265468a22.205438 22.205438 0 0 0-27.739646-14.727396l-0.228686 0.068606a22.148266 22.148266 0 0 1-27.876858-14.281459c0-0.102909-0.057172-0.205818-0.091474-0.308726l-0.388767-1.143432a22.537033 22.537033 0 0 1 14.464407-28.288493l85.059863-27.442354a22.148266 22.148266 0 0 1 27.853989 14.304327c0 0.125777 0.08004 0.240121 0.114343 0.354464l32.130423 101.948345c6.014449 19.209648 32.2562 21.382167 41.083491 3.201608l85.562972-172.921133a22.639942 22.639942 0 0 1 13.126593-11.434314l68.034169-21.953883a22.525599 22.525599 0 0 1 26.74486 31.627313z" fill="#f2a41a" p-id="4307"></path></svg>
                </base-tooltip>
                <base-tooltip v-if="'1' == scholar.isIns " content="机构用户">
                    <svg style="margin-left: 5px;" t="1697860082636" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4023" width="16" height="16"><path d="M736 812.8c-19.2 0-19.2-51.2-19.2-51.2s57.6-51.2 70.4-121.6c32 0 51.2-76.8 19.2-102.4C806.4 512 844.8 320 640 320c-25.6 0-44.8 0-64 6.4V192H320V64H0v960h1024c0-102.4-230.4-198.4-288-211.2zM256 960H64V128h192v832z m288-147.2c-6.4 0-19.2 6.4-32 6.4-51.2 19.2-134.4 51.2-192 102.4V256h192v108.8c-57.6 57.6-38.4 153.6-38.4 172.8-32 25.6-12.8 102.4 25.6 102.4 0 19.2 6.4 32 12.8 51.2 19.2 44.8 51.2 76.8 51.2 76.8s0 44.8-19.2 44.8z" fill="#1296db" p-id="4024"></path><path d="M128 192h64v64h-64v-64zM128 320h64v64h-64v-64zM384 448h64v64h-64v-64zM384 576h64v64h-64v-64zM384 704h64v64h-64v-64zM384 320h64v64h-64v-64zM128 448h64v64h-64v-64zM128 576h64v64h-64v-64zM128 704h64v64h-64v-64zM128 832h64v64h-64v-64z" fill="#1296db" p-id="4025"></path></svg>
                </base-tooltip>
            </div>
            <div v-if="showIns" class="LittleScholar__info--item LittleScholar__info--institution">{{scholar.institution}}</div>
        </div>
    </div>
</x-card>
</template>

<script>
    import {XCard,Avatar} from '@zkwq/business'
    export default {
        name:'LittleScholar',
        components:{XCard,Avatar},
        props:{
            scholar:Object,
            shadow:{
                type:String,
                default:'none'
            },
            avatarSize:{
                type:Number,
                default:50
            },
            showIns:{
                type:Boolean,
                default:true
            }
        },
        data(){
            return {

            }
        },
        methods:{
            goProfileDetail(){
                if(this.scholar.uid || this.scholar.id){
                    window.open(`/microapp/profile/${this.scholar.uid || this.scholar.id}`)
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import '@zkwq/business/dist/var.scss';
    .LittleScholar__warp{width: 100%;height: auto;overflow: hidden;display: flex;gap:16px;}
    .LittleScholar__warp--hover{cursor: pointer;}
    .LittleScholar__info{flex: 1;height: auto;overflow: hidden;width: 0px;}
    .LittleScholar__info--item{width: 100%;margin-bottom:4px;}
    .LittleScholar__info--noIns{display: flex;align-items: center;}
    .LittleScholar__info--institution{
        color:$--color-text-regular;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
    }
    .LittleScholar__info--subject{color:$--color-text-placeholder;}
</style>