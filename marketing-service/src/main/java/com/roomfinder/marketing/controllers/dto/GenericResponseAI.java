package com.roomfinder.marketing.controllers.dto;



import java.util.List;


public class GenericResponseAI {
    private List<Intent> intents;

    public List<Intent> getIntents() {
        return intents;
    }

    public void setIntents(List<Intent> intents) {
        this.intents = intents;
    }

    public static class Intent {
        private String tag;
        private List<String> patterns;
        private List<String> responses;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public List<String> getPatterns() {
            return patterns;
        }

        public void setPatterns(List<String> patterns) {
            this.patterns = patterns;
        }

        public List<String> getResponses() {
            return responses;
        }

        public void setResponses(List<String> responses) {
            this.responses = responses;
        }
    }
}
